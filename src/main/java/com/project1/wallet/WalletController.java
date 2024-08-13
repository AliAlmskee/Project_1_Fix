package com.project1.wallet;

import com.project1.transaction.TransactionService;
import com.project1.transaction.data.Transaction;
import com.project1.wallet.data.Wallet;
import com.project1.wallet.data.WalletDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wallets")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;



    @GetMapping("/{user-id}")
    public ResponseEntity<Wallet> getWalletByUserId(@PathVariable("user-id") int userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
    @GetMapping("/with_transactions/{user-id}")
    public ResponseEntity<Map<String, Object>> getWalletWithTransactions(@PathVariable("user-id") Integer userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        List<Transaction> transactions =  transactionService.getTransactionsByReceiverOrSender(userId.longValue());
        return ResponseEntity.ok(Map.of("wallet", wallet, "transactions", transactions));
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public ResponseEntity<Wallet> createWallet(@RequestBody WalletDTO wallet) {
//        Wallet newWallet = walletService.createWallet(wallet);
//        return new ResponseEntity<>(newWallet, HttpStatus.CREATED);
//    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/add-total-balance")
//    public Wallet addTotalBalance(@RequestBody AddBalanceRequest request) {
//        return walletService.addTotalBalance(request.getId(), request.getAmount());
//    }
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/subtract-total-balance")
//    public Wallet subtractTotalBalance(@RequestBody SubtractBalanceRequest request) {
//        return walletService.subtractTotalBalance(request.getId(), request.getAmount());
//    }
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/add-total-held-balance")
//    public Wallet addTotalHeldBalance(@RequestBody AddBalanceRequest request) {
//        return walletService.addTotalHeldBalance(request.getId(), request.getAmount());
//    }
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/subtract-total-held-balance")
//    public Wallet subtractTotalHeldBalance(@RequestBody SubtractBalanceRequest request) {
//        return walletService.subtractTotalHeldBalance(request.getId(), request.getAmount());
//    }
}