package com.project1.transaction.data;


import com.project1.transaction.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long senderUserId;
    private Long receiverUserId;
    private Double amount;
    private Date transactionDate;
    private TransactionType type; // Enum for deposit, withdraw, transfer, hold, unhold, transfer_held
    private Long transactionNumber;
    private Double postBalance;
}
