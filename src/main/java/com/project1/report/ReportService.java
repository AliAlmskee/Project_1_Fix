package com.project1.report;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.report.data.Report;
import com.project1.user.User;
import com.project1.user.UserDTO;
import com.project1.user.UserMapper;
import com.project1.user.UserRepository;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// ReportService.java
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ApplicationAuditAware applicationAuditAware;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Report createReport(Report report) {
        if (!reportRepository.existsBySenderIdAndRecipientId(report.getSenderId(), report.getRecipientId())) {
            return reportRepository.save(report);
        } else {
            // Handle the case where the senderId and recipientId already exist
            // For example, you could throw an exception or return null
            throw new RuntimeException("Report with senderId and recipientId already exists");
        }
    }

    public List<Report> getAllReports() {
        return reportRepository.findAllAppReport();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElseThrow();
    }

    public Report updateReport(Long id, Report report) {
        Report existingReport = getReportById(id);
        existingReport.setMassage(report.getMassage());
        existingReport.setSenderId(report.getSenderId());
        existingReport.setRecipientId(report.getRecipientId());
        return reportRepository.save(existingReport);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    public List<UserDTO> getUserReportsGreater(int X) {
        List<Integer> results = reportRepository.findUsersWithReportsGreaterThanX(X - 1 );
        List<User> users = userRepository.findAllById(results);
        List<UserDTO> userDTOS = userMapper.usersToUserDTOs(users);
        return userDTOS;
    }
}