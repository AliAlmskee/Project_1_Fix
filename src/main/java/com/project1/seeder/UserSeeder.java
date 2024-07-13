package com.project1.seeder;

import com.project1.category.Category;
import com.project1.user.Role;
import com.project1.user.User;
import com.project1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
public class UserSeeder {
    private final UserRepository userRepository;

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

          //  userRepository.saveAll(users);
        }
    }
}