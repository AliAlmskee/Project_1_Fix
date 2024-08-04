package com.project1.auditing;

import com.project1.user.User;
import com.project1.user.UserDTO;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
            !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal);
    }
    public Optional<UserDTO> getCurrentUser2() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) {
            return Optional.empty();
        }

        User userPrincipal = (User) authentication.getPrincipal();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userPrincipal.getId());
        userDTO.setFirstname(userPrincipal.getFirstname());
        userDTO.setLastname(userPrincipal.getLastname());
        userDTO.setPhone(userPrincipal.getPhone());
        userDTO.setEmail(userPrincipal.getEmail());
        userDTO.setPoints(userPrincipal.getPoints());
        userDTO.setStatus(userPrincipal.getStatus());
        userDTO.setDevice_token(userPrincipal.getDevice_token());

        return Optional.ofNullable(userDTO);
    }

}
