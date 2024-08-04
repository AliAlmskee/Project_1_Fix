package com.project1.seeder;

import com.project1.category.Category;
import com.project1.config.JwtService;
import com.project1.token.Token;
import com.project1.token.TokenRepository;
import com.project1.token.TokenType;
import com.project1.user.Role;
import com.project1.user.User;
import com.project1.user.UserRepository;
import com.project1.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class UserSeeder {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final WalletService walletService;

    public void seed() {
        if (userRepository.count() == 0) {
            List<User> users = new ArrayList<>();

            User admin1 = new User();
            admin1.setFirstname("John");
            admin1.setLastname("Doe");
            admin1.setEmail("johndoe@example.com");
            admin1.setPhone("0987651321");
            admin1.setPassword("securePassword123");
            admin1.setRole(Role.ADMIN);
            users.add(admin1);

            User admin2 = new User();
            admin2.setFirstname("Jane");
            admin2.setLastname("Smith");
            admin2.setEmail("janesmith@example.com");
            admin2.setPhone("0934567890");
            admin2.setPassword("anotherSecurePassword456");
            admin2.setRole(Role.ADMIN);
            users.add(admin2);


            User client = new User();
            client.setFirstname("Kara");
            client.setLastname("Pham");
            client.setEmail("KaraPham23s@example.com");
            client.setPhone("0912312345");
            client.setPassword(passwordEncoder.encode("secure+password"));
            client.setRole(Role.CLIENT);
            users.add(client);

            User worker = new User();
            worker.setFirstname("Mavis");
            worker.setLastname("Wilkins");
            worker.setEmail("MavisWilkins98@example.com");
            worker.setPhone("0912345645");
            worker.setPassword(passwordEncoder.encode("secure+password"));
            worker.setRole(Role.WORKER);
            users.add(worker);

            userRepository.saveAll(users);
            for (User savedUser : users) {
                walletService.createNewWallet(savedUser);
            }
            makeToken(client);
            makeToken(worker);
        }
    }

    void makeToken(User user){
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        System.out.println(jwtToken);
        tokenRepository.save(token);
    }
}