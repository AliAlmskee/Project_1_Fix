package com.project1.statisitc;

import com.project1.transaction.TransactionRepository;
import com.project1.transaction.TransactionType;
import com.project1.transaction.data.Transaction;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class Statistic {
    private final TransactionRepository transactionRepository;

    public Map<Date, Double> getDailyDepositAmountsForPastWeek() {
      Date today = new java.sql.Date(System.currentTimeMillis());
      Date aWeekAgo = new java.sql.Date(today.getTime() - 7 * 24 * 60 * 60 * 1000);

        Map<Date, Double> dailyAmounts = new HashMap<>();

        for (Date date = aWeekAgo; date.before(today); date = new java.sql.Date(date.getTime() + 24 * 60 * 60 * 1000)) {
            double amount = 0;
            for (Transaction transaction : transactionRepository.findByTypeAndTransactionDate(TransactionType.DEPOSIT, date)) {
                amount += transaction.getAmount() * 0.05;
            }
            dailyAmounts.put(date, amount);
        }

        return dailyAmounts;
    }
}
