package com.project1.fileSystem;

import com.project1.job.data.Job;
import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Doc {
    @Id
    @GeneratedValue
    private Long id;
    String doc ;

    @ManyToMany(mappedBy = "docs")
    private List<ClientProfile> clientProfiles;

    @ManyToMany(mappedBy = "docs")
    private List<WorkerProfile> workerProfiles;


    @ManyToMany(mappedBy = "docs")
    private List<Job> jobs;

}
