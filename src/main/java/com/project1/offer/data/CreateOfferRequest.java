package com.project1.offer.data;

import java.util.Date;
import java.util.Set;

public record CreateOfferRequest(
        String message,
        Long cost,
        Long deliveryTime,
        Long workerId,
        Long projectId
){}
