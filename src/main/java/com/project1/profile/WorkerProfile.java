package com.project1.profile;

import com.project1.category.Category;
import com.project1.fileSystem.Photo;
import com.project1.job.data.Job;
import com.project1.jobTitle.JobTitle;
import com.project1.skill.Skill;
import com.project1.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class WorkerProfile {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

    String bio;

    double rate;

    boolean is_verified;

    @ManyToOne
    @JoinColumn(name = "jobTitleId")
    JobTitle jobTitle;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    Category category;


    @ManyToMany
    @JoinTable(
            name = "worker_profile_photos",
            joinColumns = @JoinColumn(name = "worker_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private List<Photo> photos;

    @ManyToMany
    @JoinTable(
            name = "worker_profile_skills",
            joinColumns = @JoinColumn(name = "worker_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

    @OneToMany(mappedBy = "workerProfile")
    private List<Job> jobs;

}