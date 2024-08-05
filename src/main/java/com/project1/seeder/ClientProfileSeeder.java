package com.project1.seeder;

import com.project1.category.Category;
import com.project1.jobTitle.JobTitle;
import com.project1.profile.ClientProfile;
import com.project1.profile.ClientProfileRepository;
import com.project1.skill.Skill;
import com.project1.user.User;
import com.project1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientProfileSeeder {
    private final ClientProfileRepository clientProfileRepository;

    public void seed(User user) {
        if (clientProfileRepository.count() == 0) {
            List<ClientProfile> profiles = List.of(
                    ClientProfile.builder()
                            .bio("Client Profile Bio")
                            .rate(0)
                            .skills(List.of(Skill.builder().id(1L).build(), Skill.builder().id(2L).build()))
                            .jobTitle(JobTitle.builder().id(1L).build())
                            .photos(List.of())
                            .user(user)
                            .build()
            );
            clientProfileRepository.saveAll(profiles);
        }
    }
}
