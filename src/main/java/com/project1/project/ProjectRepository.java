package com.project1.project;

import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.project.data.Project;
import com.project1.project.data.ProjectResponse;
import com.project1.project.data.ProjectStatus;
import com.project1.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByClient_UserOrWorker_User(User user, User user1);

    List<Project> findAllByClient(ClientProfile client);

    List<Project> findAllByWorker(WorkerProfile worker);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Project p SET p.status = :status, p.worker = (SELECT o3.worker FROM Offer o3 WHERE o3.id = :id) WHERE p = (SELECT o2.project FROM Offer o2 WHERE o2.id = :id)")
//    int updateStatusByOfferId(@Param("status") ProjectStatus status,@Param("id") Long id);

    boolean existsByClient_UserId(Integer userId);

    boolean deleteByIdAndStatus(Long projectId, ProjectStatus projectStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.status = :newStatus WHERE p.id = :id AND p.status = :oldStatus")
    boolean updateStatusByProjectIdAndStatus(@Param("newStatus") ProjectStatus newStatus, @Param("id") Long id, @Param("oldStatus") ProjectStatus oldStatus);

    @Query("SELECT o2.project FROM Offer o2 WHERE o2.id = :id")
    Optional<Project> findByOfferId(@Param("id") Long id);
}