package com.project1.rate;

import java.util.Date;

public record RateDTO (
    Long id,
    Date createDate,
    String description,
    Double totalRate,
    RatedType rated,
    Long clientProfileId,
    Long workerProfileId
){
}
