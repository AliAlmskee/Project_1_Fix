package com.project1.rate;

import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

    int countByClient(ClientProfile clientProfile);

    int countByWorker(WorkerProfile workerProfile);

    List<Rate> findByClient_Id(Long profileId);

    List<Rate> findByClient_IdAndRated(Long profileId, RatedType profileType);

    List<Rate> findByWorker_IdAndRated(Long profileId, RatedType profileType);
}