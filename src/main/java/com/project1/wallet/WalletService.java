package com.project1.wallet;

import com.project1.wallet.WalletRepository;
import com.project1.wallet.data.Wallet;
import com.project1.wallet.data.WalletDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private WalletMapper walletMapper;


    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletById(Long id) {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);
        return optionalWallet.orElseThrow();
    }

    public Wallet createWallet(WalletDTO walletDTO) {
        return walletRepository.save(walletMapper.walletDTOToWallet(walletDTO));
    }

    public Wallet updateWallet(Long id, Wallet wallet) {
        Wallet existingWallet = getWalletById(id);
        existingWallet.setTotalBalance(wallet.getTotalBalance());
        existingWallet.setTotalHeldBalance(wallet.getTotalHeldBalance());
        return walletRepository.save(existingWallet);
    }

    public void deleteWallet(Long id) {
        walletRepository.deleteById(id);
    }
}