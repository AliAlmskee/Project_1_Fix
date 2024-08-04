package com.project1.wallet;

import com.project1.wallet.data.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(int id);
    Optional<Wallet> findById(Long id);

}