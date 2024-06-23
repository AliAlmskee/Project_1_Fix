package com.security.auth;

import com.security.Sms.SMSService;
import com.security.config.JwtService;
import com.security.token.Token;
import com.security.token.TokenRepository;
import com.security.token.TokenType;
import com.security.user.Role;
import com.security.user.Status;
import com.security.user.User;
import com.security.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.verification.VerificationRepository;
import com.security.verification.Verification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

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
  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .phone(request.getPhone())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .points(0)
        .status(Status.ACTIVE)
        .build();

    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public String firstStepLogin(FirstStepLoginRequest request) throws Exception {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getPhone(),
            request.getPassword()
        )
    );
    var user = repository.findByPhone(request.getPhone())
   .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number not valid"));

    if(user.getRole()== Role.ADMIN)
    {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "role is not correct");
    }

    Verification newVerificationCode = new Verification();
    int min = 100000;
    int max = 999999;
    String finalCode;
    int code;
  //  do {
       code = (int) (Math.random() * ((max - min) + 1)) + min;
      finalCode = Integer.toString(code);
  //  } while (verificationRepository.findByCode(finalCode) != null);

    newVerificationCode.setCode(finalCode);
    newVerificationCode.setUser(user);
    user.getVerification_codes().add(newVerificationCode);
    verificationRepository.save(newVerificationCode);
     smsService.sendSMS(user,code," Your enjas Verification Code");
    return " A  code has been sent to your phone ";

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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email number not valid"));
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
