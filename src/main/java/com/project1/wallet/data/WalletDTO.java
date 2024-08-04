package com.project1.wallet.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletDTO {

    private double totalBalance ;

    private double totalHeldBalance;

}
