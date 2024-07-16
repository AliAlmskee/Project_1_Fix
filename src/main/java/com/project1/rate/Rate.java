package com.project1.rate;

import com.project1.profile.ClientProfile;
import com.project1.profile.WorkerProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Date createDate;
    private String description;
    @ManyToOne
    private ClientProfile client;
    @ManyToOne
    private WorkerProfile worker;
}
