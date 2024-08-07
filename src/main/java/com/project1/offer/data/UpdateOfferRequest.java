package com.project1.offer.data;

public record UpdateOfferRequest(
        String message,
        Long cost,
        Long deliveryTime
){}
