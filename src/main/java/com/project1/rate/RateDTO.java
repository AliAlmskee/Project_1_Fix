package com.project1.rate;

import com.project1.profile.ClientProfile;
import com.project1.profile.ClientProfileDTO;
import com.project1.profile.WorkerProfile;
import com.project1.profile.WorkerProfileDTO;

import java.util.Date;

public record RateDTO (
    Long id,
    Date createDate,
    String description,
    Double totalRate,
    RatedType rated,
    ClientProfileDTO client,
    WorkerProfileDTO worker
){
}
