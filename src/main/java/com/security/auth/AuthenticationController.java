package com.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
    @Valid @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/first-step-login")
  public String firstAuthenticate(
      @RequestBody FirstStepLoginRequest request
  ) throws Exception {
    return service.firstStepLogin(request);
  }

  @PostMapping("/second-step-login")
  public AuthenticationResponse secondAuthenticate(
          @RequestBody SecondStepLoginRequest request
  ) {
    return service.secondStepLogin(request);
  }


  @PostMapping("/admin-login")
  public ResponseEntity<AuthenticationResponse> admin_authenticate(
          @RequestBody AdminLoginRequest request
  ) {
    return ResponseEntity.ok(service.adminLogin(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }



}
