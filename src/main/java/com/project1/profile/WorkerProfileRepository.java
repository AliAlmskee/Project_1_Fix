package com.project1.profile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long> {
    List<WorkerProfile> findAllByUserId(Long userId);

}