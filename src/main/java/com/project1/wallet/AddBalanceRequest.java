package com.project1.wallet;

import lombok.Data;

@Data
public class AddBalanceRequest {
    private Long id;
    private double amount;
}
