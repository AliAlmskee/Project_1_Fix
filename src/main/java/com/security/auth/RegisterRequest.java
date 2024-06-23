package com.security.auth;

import com.security.user.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  @Valid

  @NotNull @NotBlank
  private String firstname;

  @NotNull @NotBlank
  private String lastname;

  @NotNull @NotBlank
  @Pattern(regexp = ".+@.+\\..+", message = "Email should be valid")
  private String email;


  @NotNull @NotBlank
  private String password;

  @Pattern(regexp = "09\\d{8}", message = "phone should be valid syrian phone")
  private String phone;


  private Role role;
}
