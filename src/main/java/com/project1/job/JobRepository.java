package com.project1.job;

import com.project1.job.data.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findAllByWorkerProfileId(Long workerProfileId);
}