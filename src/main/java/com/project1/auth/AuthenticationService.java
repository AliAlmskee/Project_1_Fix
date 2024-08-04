package com.project1.auth;

import com.project1.sms.SMSService;
import com.project1.config.JwtService;
import com.project1.token.Token;
import com.project1.token.TokenRepository;
import com.project1.token.TokenType;
import com.project1.user.Role;
import com.project1.user.Status;
import com.project1.user.User;
import com.project1.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.verification.Verification;
import com.project1.verification.VerificationRepository;
import com.project1.wallet.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final VerificationRepository verificationRepository;

  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final SMSService smsService;
  private final WalletService walletService;

  public ResponseEntity<String> register(RegisterRequest request) throws Exception {
    var user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .phone(request.getPhone())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .points(0)
            .status(Status.ACTIVE)
            .device_token(request.getDevice_token())
            .build();

    var savedUser = repository.save(user);
    walletService.createNewWallet(savedUser);

    if(user.getRole()==Role.ADMIN)
      return ResponseEntity.ok("Register successfully");
    int code = sendVerificationSMS(user);

    return ResponseEntity.ok( "A code has been sent to your phone: " + code);
  }

  public ResponseEntity<String> firstStepLogin(FirstStepLoginRequest request) throws Exception {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getPhone(),
                      request.getPassword()
              )
      );
    } catch (AuthenticationException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong credential", e);
    }
    var user = repository.findByPhone(request.getPhone())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number not valid"));

    if(user.getRole()== Role.ADMIN  )
    {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "role is not correct");
    }

    int code = sendVerificationSMS(user);
    return ResponseEntity.ok("A code has been sent to your phone: " + code);

  }
  private int sendVerificationSMS(User user) throws Exception {
    Verification newVerificationCode = new Verification();
    int min = 100000;
    int max = 999999;
    String finalCode;
    int code;

    code = (int) (Math.random() * ((max - min) + 1)) + min;
    finalCode = Integer.toString(code);

    newVerificationCode.setCode(finalCode);
    newVerificationCode.setUser(user);

    if (user.getVerification_codes() == null) {
      user.setVerification_codes(new ArrayList<>());
    }

    user.getVerification_codes().add(newVerificationCode);
    verificationRepository.save(newVerificationCode);
    smsService.sendSMS(user, code, " Your enjas Verification Code");
    return code;
  }



  public AuthenticationResponse secondStepLogin(SecondStepLoginRequest request) {

    var code = verificationRepository.findByCode(request.getCode())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "code  not valid"));


    User user  = code.getUser();
    verificationRepository.delete(code);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    // revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();

  }

  public AuthenticationResponse adminLogin(AdminLoginRequest request) {


    User user = (User) repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email  not valid"));
    if(user.getRole()!= Role.ADMIN)
    {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "role is not admin");
    }
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is not correct");
    }

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    //  revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userPhone;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userPhone = jwtService.extractUsername(refreshToken);
    if (userPhone != null) {
      var user = this.repository.findByPhone(userPhone)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}