package com.project1.transaction;

import com.project1.transaction.data.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.threeten.bp.LocalDate;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderUserId(Long senderId);
    List<Transaction> findByReceiverUserId(Long receiverId);
    List<Transaction> findByType(TransactionType type);

    Object findByTransactionNumber(long transactionNumber);

    List<Transaction> findByReceiverUserIdOrSenderUserId(Long userId, Long userId1);

    List<Transaction> findByTypeAndTransactionDate(TransactionType type, Date date);
}