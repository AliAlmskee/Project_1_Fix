package com.project1.offer.data;

import com.project1.profile.WorkerProfileDTO;
import lombok.Builder;

import java.util.Date;

@Builder
public record OfferResponse(
        Integer id,
        String message,
        Long cost,
        Long deliveryTime,
        OfferStatus status,
        Date createDate,
        WorkerProfileDTO worker
){}
