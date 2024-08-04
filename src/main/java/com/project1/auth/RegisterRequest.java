package com.project1.auth;
import valiadtion.ValidateEmployeeType;
import validation.Unique;

import com.project1.user.Role;
import com.project1.user.User;
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


  @ValidateEmployeeType
  @Unique(entity = User.class, fieldName = "firstname")
  private String firstname;

  @NotNull
  @NotBlank
  private String lastname;

  @NotNull
  @NotBlank
  @Pattern(regexp = ".+@.+\\..+", message = "Email should be valid")
  private String email;

  @NotNull
  @NotBlank
  private String password;

  @Pattern(regexp = "09\\d{8}", message = "Phone should be a valid Syrian phone number")
  private String phone;

  private Role role;

  @NotNull
  @NotBlank
  private String device_token;

}
