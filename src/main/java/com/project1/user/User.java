package com.project1.user;

import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.token.Token;
import com.project1.verification.Verification;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;


  private String lastname;

  @Column(unique = true)
  private String phone;

  private String email;

  private String password;
  private Integer points;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;


  @OneToMany(mappedBy = "user")
  private List<Verification> Verification_codes;

  @OneToMany(mappedBy = "user")
  private List<WorkerProfile> workerProfiles;

  @OneToMany(mappedBy = "user")
  private List<ClientProfile> clientProfiles;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return phone;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
