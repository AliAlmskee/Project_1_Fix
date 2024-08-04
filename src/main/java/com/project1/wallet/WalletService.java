package com.project1.wallet;

import com.project1.user.User;
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


   // public List<Wallet> getAllWallets() {
    //    return walletRepository.findAll();
  //  }

    public Wallet getWalletByUserId(int id) {
        Optional<Wallet> optionalWallet = walletRepository.findByUserId(id);
        return optionalWallet.orElseThrow();
    }

    public Wallet createNewWallet(User user){
        WalletDTO walletDTO = new WalletDTO(0.0, 0.0);
        Wallet wallet = walletMapper.walletDTOToWallet(walletDTO);
        wallet.setUser(user);
        user.setWallet(wallet);
        return walletRepository.save(wallet);
    }



    public Wallet addTotalBalance(Long id, double amount) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(id);
        if (existingWalletOptional.isPresent()) {
            Wallet existingWallet = existingWalletOptional.get();
            existingWallet.setTotalBalance(existingWallet.getTotalBalance()+ amount);
            return walletRepository.save(existingWallet);
        } else {
            throw new RuntimeException("Wallet with id " + id + " not found");
        }
    }

    public Wallet subtractTotalBalance(Long id, double amount) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(id);
        if (existingWalletOptional.isPresent()) {
            Wallet existingWallet = existingWalletOptional.get();
            if (existingWallet.getTotalBalance()  + amount >= 0) {
                existingWallet.setTotalBalance(existingWallet.getTotalBalance() - amount);
                return walletRepository.save(existingWallet);
            } else {
                throw new RuntimeException("Insufficient balance");
            }
        } else {
            throw new RuntimeException("Wallet with id " + id + " not found");
        }
    }

    public Wallet addTotalHeldBalance(Long id, double amount) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(id);
        if (existingWalletOptional.isPresent()) {
            Wallet existingWallet = existingWalletOptional.get();
            existingWallet.setTotalHeldBalance(existingWallet.getTotalHeldBalance()+ amount);
            return walletRepository.save(existingWallet);
        } else {
            throw new RuntimeException("Wallet with id " + id + " not found");
        }
    }

    public Wallet subtractTotalHeldBalance(Long id, double amount) {
        Optional<Wallet> existingWalletOptional = walletRepository.findById(id);
        if (existingWalletOptional.isPresent()) {
            Wallet existingWallet = existingWalletOptional.get();
            if (existingWallet.getTotalHeldBalance()- amount >= 0) {
                existingWallet.setTotalHeldBalance(existingWallet.getTotalHeldBalance()-amount);
                return walletRepository.save(existingWallet);
            } else {
                throw new RuntimeException("Insufficient held balance");
            }
        } else {
            throw new RuntimeException("Wallet with id " + id + " not found");
        }
    }

}