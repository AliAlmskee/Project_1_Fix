package com.project1.transaction.data;

import com.project1.transaction.TransactionType;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDTO {
    private Long id;
    private Long senderUserId;
    private String senderUserName;
    private Long receiverUserId;
    private String receiverUserName;
    private Double amount;
    private Date transactionDate;
    private TransactionType type; // Enum for deposit, withdraw, transfer, hold, unhold, transfer_held
    private Long transactionNumber;
}
