package com.project1.project.data;

import com.project1.category.Category;
import com.project1.project.data.ProjectStatus;
import com.project1.skill.Skill;
import com.project1.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name="client_id")
    private User client;
    @ManyToOne
    @JoinColumn(name="worker_id")
    private User worker;
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
}
