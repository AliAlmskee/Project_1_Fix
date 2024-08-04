package com.project1.category;

import com.project1.profile.WorkerProfile;
import com.project1.token.Token;
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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String photoPath;

    @OneToMany(mappedBy = "category")
    private List<WorkerProfile> workerProfiles;


}