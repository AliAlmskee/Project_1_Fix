package com.project1.project.data;

import com.project1.category.Category;
import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import com.project1.skill.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Long minBudget;
    @NotNull
    private Long maxBudget;
    @NotNull
    private Long ExpectedDuration;
    @NotNull
    private ProjectStatus status;
    @NotNull
    private Date createDate;
    @ManyToOne
    @JoinColumn(name="client_profile_id")
    private ClientProfile client;
    @ManyToOne
    @JoinColumn(name="worker_profile_id")
    private WorkerProfile worker;
//    @JoinTable(
//            name="ProjectCategory",
//            joinColumns = @JoinColumn(name = "project_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id")
//    )
    @ManyToOne
    @NotNull
    private Category projectCategory;
    @ManyToMany
    @JoinTable(
            name="ProjectSkill",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @NotNull
    private Set<Skill> projectSkill;

    @Formula("(SELECT COUNT(*) FROM Offer offer WHERE offer.project_id = id)")
    @ReadOnlyProperty
    private Integer offerCount;
}
