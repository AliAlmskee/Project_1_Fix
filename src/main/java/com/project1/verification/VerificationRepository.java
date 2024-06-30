package com.project1.verification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Integer> {
    Optional<Verification> findByCode(String verification_code);
}
