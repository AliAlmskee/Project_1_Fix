package com.project1.offer;

import com.project1.offer.data.Offer;
import com.project1.offer.data.OfferStatus;
import com.project1.profile.WorkerProfile;
import com.project1.project.data.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findAllByProject(Project project);

    List<Offer> findAllByProjectId(Long projectId);

    @Modifying
    @Transactional
    @Query("UPDATE Offer o SET o.status = :status WHERE o.id = :id")
    int updateStatusById(@Param("status") OfferStatus status,@Param("id") Long id);
    @Modifying
    @Transactional
    @Query("UPDATE Offer o SET o.status = :status WHERE o.project = (SELECT o2.project FROM Offer o2 WHERE o2.id = :id)")
    int updateStatusOfSameProject(@Param("status") OfferStatus status, @Param("id") Long id);

    List<Offer> findAllByWorker(WorkerProfile user);

    boolean existsByProject_Client_UserId(Integer id);

    boolean deleteByIdAndStatus(Long projectId, OfferStatus offerStatus);

    boolean existsByWorker_UserId(Integer userId);

    boolean existsByIdAndStatus(Long id, OfferStatus offerStatus);
}