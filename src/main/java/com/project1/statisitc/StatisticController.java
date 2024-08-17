package com.project1.statisitc;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.LocalDate;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticController {
    private final Statistic statistic;



    @GetMapping("/daily-deposits")
    public ResponseEntity<Map<Date, Double>> getDailyDepositAmountsForPastWeek() {
        Map<Date, Double> dailyAmounts = statistic.getDailyDepositAmountsForPastWeek();
        return ResponseEntity.ok(dailyAmounts);
    }
}