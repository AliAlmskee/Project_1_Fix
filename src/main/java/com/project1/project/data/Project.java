package com.project1.project.data;

import com.project1.category.Category;
import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.project.data.ProjectStatus;
import com.project1.skill.Skill;
import com.project1.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private Long minBudget;
    private Long maxBudget;
    private Long ExpectedDuration;
    private ProjectStatus status;
    private Date createDate;
    @ManyToOne
    @JoinColumn(name="client_profile_id")
    private ClientProfile client;
    @ManyToOne
    @JoinColumn(name="worker_profile_id")
    private WorkerProfile worker;
    @ManyToMany
    @JoinTable(
            name="ProjectCategory",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> projectCategories;
    @ManyToMany
    @JoinTable(
            name="ProjectSkill",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> projectSkill;

    @Formula("(SELECT COUNT(*) FROM Offer offer WHERE offer.project_id = id)")
    @ReadOnlyProperty
    private Integer offerCount;
}
