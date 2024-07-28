package com.project1.project;

import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.project.data.Project;
import com.project1.project.data.ProjectResponse;
import com.project1.project.data.ProjectStatus;
import com.project1.project.data.ProjectWithOfferCount;
import com.project1.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

//    String search, List<Long> categories, List<Long> skills,
//    Long minBudget, Long maxBudget, Long duration,
//    ProjectStatus status,
//    ProjectSortTypes sortBy, Boolean sortDes,

    @Query(value = "SELECT p.*, " +
            "(SELECT COUNT(*) FROM OrderOffer oo WHERE oo.order_id = p.id) AS offerCount " +
            "FROM Project p " +
            "JOIN ProjectCategory pc ON p.id = pc.project_id " +
            "JOIN ProjectSkill ps ON p.id = ps.project_id " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :namePattern, '%')) " +
            "AND pc.category_id IN (:categoryIds) " +
            "AND ps.id IN (:skillIds) " +
            "AND ((p.minBudget BETWEEN :minBudget AND :maxBudget) " +
            "OR (p.maxBudget BETWEEN :minBudget AND :maxBudget) " +
            "OR (:minBudget BETWEEN p.minBudget AND p.maxBudget) " +
            "OR (:maxBudget BETWEEN p.minBudget AND p.maxBudget)) " +
            "AND p.ExpectedDuration <= :duration " +
            "AND p.status = :status " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'CreateDate' THEN p.createDate END, " +
            "CASE WHEN :sortBy = 'Duration' THEN p.ExpectedDuration END, " +
            "CASE WHEN :sortBy = 'Budget' THEN p.minBudget END, " +
            "CASE WHEN :sortBy = 'NoOfOffers' THEN offerCount END " +
            ":sortDirection",
            nativeQuery = true)
    List<ProjectWithOfferCount> findFilteredProjects(@Param("namePattern") String namePattern,
                                                     @Param("categoryIds") List<Long> categoryIds,
                                                     @Param("skillIds") List<Long> skillIds,
                                                     @Param("minBudget") Long minBudget,
                                                     @Param("maxBudget") Long maxBudget,
                                                     @Param("duration") Long duration,
                                                     @Param("status") ProjectStatus status,
                                                     @Param("sortBy") String sortBy,
                                                     @Param("sortDirection") String sortDirection);

//    List<Project> findAllProjects();

//    @Query(value = ":query", nativeQuery = true)
//    List<ProjectWithOfferCount> findFilteredProjectsQ(@Param("query") String query);

    List<Project> findAllByClient_UserOrWorker_User(User user, User user1);

    List<Project> findAllByClient(ClientProfile client);

    List<Project> findAllByWorker(WorkerProfile worker);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Project p SET p.status = :status, p.worker = (SELECT o3.worker FROM Offer o3 WHERE o3.id = :id) WHERE p = (SELECT o2.project FROM Offer o2 WHERE o2.id = :id)")
//    int updateStatusByOfferId(@Param("status") ProjectStatus status,@Param("id") Long id);

    boolean existsByClient_UserId(Integer userId);

    Integer deleteByIdAndStatus(Long projectId, ProjectStatus projectStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.status = :newStatus WHERE p.id = :id AND p.status = :oldStatus")
    Integer updateStatusByProjectIdAndStatus(@Param("newStatus") ProjectStatus newStatus, @Param("id") Long id, @Param("oldStatus") ProjectStatus oldStatus);

    @Query("SELECT o2.project FROM Offer o2 WHERE o2.id = :id")
    Optional<Project> findByOfferId(@Param("id") Long id);
}