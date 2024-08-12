package com.project1.report;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.report.data.Report;
import com.project1.user.User;
import com.project1.user.UserDTO;
import com.project1.user.UserMapper;
import com.project1.user.UserRepository;
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
       // report.setSenderId(applicationAuditAware.getCurrentAuditor().get());
        return reportRepository.save(report);
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