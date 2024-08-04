package com.project1.transaction;

import com.project1.transaction.data.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderUserId(Long senderId);
    List<Transaction> findByReceiverUserId(Long receiverId);
    List<Transaction> findByType(TransactionType type);
}