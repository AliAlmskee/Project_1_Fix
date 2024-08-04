package com.project1.jobTitle;

import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "jobTitle")
    private List<ClientProfile> clientProfiles;

    @OneToMany(mappedBy = "jobTitle")
    private List<WorkerProfile> workerProfiles;


}
