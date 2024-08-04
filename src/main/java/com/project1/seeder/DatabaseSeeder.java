package com.project1.seeder;

import com.project1.user.User;
import com.project1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final JobTitleSeeder jobTitleSeeder;
    private final CategorySeeder categorySeeder;
    private final SkillSeeder skillSeeder;
    private final UserSeeder userSeeder;

    @Override
    public void run(String... args) throws Exception {
                jobTitleSeeder.seed();
                categorySeeder.seed();
                skillSeeder.seed();
               // userSeeder.seed();

    }
}
