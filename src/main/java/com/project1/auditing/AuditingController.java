package com.project1.auditing;

import com.project1.user.User;
import com.project1.user.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auditor")

public class AuditingController {
    private final ApplicationAuditAware auditAware;

    @GetMapping("/current-auditor-id")
    public ResponseEntity<Integer> getCurrentAuditorId() {
        Optional<Integer> auditorId = auditAware.getCurrentAuditor();
        return auditorId.map(id -> new ResponseEntity<>(id, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Optional<UserDTO> currentUser = auditAware.getCurrentUser2();
        return currentUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

}
