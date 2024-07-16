package com.project1.offer.data;

import com.project1.profile.WorkerProfile;
import com.project1.project.data.Project;
import com.project1.projectProgress.ProjectProgress;
import com.project1.rate.Rate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String message;
    private Long cost;
    private Long deliveryTime;
    private OfferStatus status;
    private Date createDate;
    @ManyToOne
    @JoinColumn(name="worker_profile_id")
    private WorkerProfile worker;
    @ManyToOne
    @JoinColumn(name = "projectId")
    @Lazy
    private Project project;
    @OneToOne
    @JoinColumn(name = "projectProgress")
    @Nullable
    private ProjectProgress projectProgress;
}
