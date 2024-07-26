package com.project1.wallet;

import lombok.Data;

@Data
public class SubtractBalanceRequest {
    private Long id;
    private double amount;
}
