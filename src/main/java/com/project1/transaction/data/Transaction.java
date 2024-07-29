package com.project1.transaction.data;


import com.project1.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import validation.Unique;

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
    private TransactionType type; // Enum for deposit, withdraw, transfer, hold, unhold
    @Column(unique = true)
    private Long transactionNumber;
    private Double postBalance;
}
