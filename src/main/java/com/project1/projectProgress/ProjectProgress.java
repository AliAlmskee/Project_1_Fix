package com.project1.projectProgress;

import com.project1.offer.data.Offer;
import com.project1.rate.Rate;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProjectProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Date acceptDate;
    @Nullable
    private Date deliveredAt;
    @Nullable
    @OneToOne
    private Rate clientRate;
    @Nullable
    @OneToOne
    private Rate workerRate;

    @ReadOnlyProperty
    @OneToOne
    private Offer offer;
}
