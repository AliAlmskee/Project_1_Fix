package com.project1.skill;

import com.project1.category.Category;
import com.project1.job.data.Job;
import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;



    @ManyToMany(mappedBy = "skills")
    private List<ClientProfile> clientProfiles;

    @ManyToMany(mappedBy = "skills")
    private List<WorkerProfile> workerProfiles;
    @ManyToMany(mappedBy = "skills")
    private List<Job> jobs;
}
