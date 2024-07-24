package com.project1.wallet;

import com.project1.wallet.data.Wallet;
import com.project1.wallet.data.WalletDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;



    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Long id) {
        Wallet wallet = walletService.getWalletById(id);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletDTO wallet) {
        Wallet newWallet = walletService.createWallet(wallet);
        return new ResponseEntity<>(newWallet, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Wallet> updateWallet(@PathVariable Long id, @RequestBody Wallet wallet) {
        Wallet updatedWallet = walletService.updateWallet(id, wallet);
        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}