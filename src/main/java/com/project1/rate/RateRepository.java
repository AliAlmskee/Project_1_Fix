package com.project1.rate;

import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rate, Long> {

    int countByClient(ClientProfile clientProfile);

    int countByWorker(WorkerProfile workerProfile);
}