package com.project1.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

  USER(Collections.emptySet()),
  ADMIN(
          Set.of(
                  Permission.ADMIN_READ,
                  Permission.ADMIN_UPDATE,
                  Permission.ADMIN_DELETE,
                  Permission.ADMIN_CREATE
          )
  ),
  WORKER(
          Set.of(

          )
  ),
  CLIENT(
          Set.of(

          )
  ),
  CLIENT_WORKER(
          combinePermissions(WORKER.getPermissions(), CLIENT.getPermissions())
  );

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }


    private static Set<Permission> combinePermissions(Set<Permission> set1, Set<Permission> set2) {
        Set<Permission> combinedPermissions = new HashSet<>(set1);
        combinedPermissions.addAll(set2);
        return combinedPermissions;
    }

}
