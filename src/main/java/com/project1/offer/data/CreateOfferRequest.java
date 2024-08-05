package com.project1.offer.data;

public record CreateOfferRequest(
        String message,
        Long cost,
        Long deliveryTime,
        Long workerId,
        Long projectId
){}
