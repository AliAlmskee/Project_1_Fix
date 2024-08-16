package com.project1.report;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.report.data.Report;
import com.project1.report.data.ReportWithUserDTO;
import com.project1.user.User;
import com.project1.user.UserDTO;
import com.project1.user.UserMapper;
import com.project1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// ReportService.java
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ApplicationAuditAware applicationAuditAware;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Report createReport(Report report) {
        if (report.getRecipientId() != null && reportRepository.existsBySenderIdAndRecipientId(report.getSenderId(), report.getRecipientId())) {
            throw new RuntimeException("Report with senderId and recipientId already exists");
        }
            return reportRepository.save(report);
    }

    public List<ReportWithUserDTO> getAllReports() {
        List<Report> reports = reportRepository.findAllAppReport();
        return reports.stream()
                .map(report -> new ReportWithUserDTO(
                        report.getId(),
                        report.getMassage(),
                        userMapper.toDto(userRepository.findById(report.getSenderId()).orElseThrow()),
                        report.getRecipientId() != null ? userMapper.toDto(userRepository.findById(report.getRecipientId()).orElseThrow()) : null
                ))
                .collect(Collectors.toList());
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElseThrow();
    }

    public   List<Report>  getReportByUserId(Integer id) {
        return reportRepository.findByRecipientId(id);
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