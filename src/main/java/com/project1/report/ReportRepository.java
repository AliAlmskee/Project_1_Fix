package com.project1.report;

import com.project1.report.data.Report;
import com.project1.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.recipientId IS NULL")
    List<Report> findAllAppReport();

    @Query("SELECT r.recipientId FROM Report r GROUP BY r.recipientId HAVING COUNT(r) > :X")
    List<Integer> findUsersWithReportsGreaterThanX(@Param("X") int X);
}