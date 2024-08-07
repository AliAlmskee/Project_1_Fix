package com.project1.projectProgress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long> {

    Optional<ProjectProgress> findByOffer_ProjectId(Long projectId);
}