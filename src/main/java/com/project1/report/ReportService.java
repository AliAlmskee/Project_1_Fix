package com.project1.report;

import com.project1.report.data.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// ReportService.java
@Service
@RequiredArgsConstructor
public class ReportService {

    private ReportRepository reportRepository;

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
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
}