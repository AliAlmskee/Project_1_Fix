package com.project1.transaction.data;


import com.project1.transaction.TransactionType;
import com.project1.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.ReadOnlyProperty;
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
    @ReadOnlyProperty
    @Formula("(SELECT u FROM User u where u.id = senderUserId)")
    private User sender;
    @ReadOnlyProperty
    @Formula("(SELECT u FROM User u where u.id = receiverUserId)")
    private User receiver;
    private Long senderUserId;
    private Long receiverUserId;
    private Double amount;
    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // Enum for deposit, withdraw, transfer, hold, unhold
    @Column(unique = true)
    private Long transactionNumber;
    private Double postBalance;
}
