package com.project1.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project1.job.data.Job;
import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.token.Token;
import com.project1.verification.Verification;
import com.project1.wallet.data.Wallet;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Integer id;
  private String firstname;


  private String lastname;

  @Column(unique = true)
  private String phone;

  private String email;

  private String password;
  private Integer points;

  private String device_token;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;


  @OneToMany(mappedBy = "user")
  private List<Verification> Verification_codes;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wallet_id", referencedColumnName = "id")
  @JsonManagedReference
  private Wallet wallet;


  @OneToMany(mappedBy = "user")
  private List<WorkerProfile> workerProfiles;

  @OneToMany(mappedBy = "user")
  private List<ClientProfile> clientProfiles;

  @ManyToMany(mappedBy = "viewedBy")
  private List<Job> viewedJobs;

  @ManyToMany(fetch = FetchType.EAGER)
  private List<Job>   likedJobs;

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
