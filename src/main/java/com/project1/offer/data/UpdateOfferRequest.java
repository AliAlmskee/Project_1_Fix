package com.project1.offer.data;

import java.util.Set;

public record UpdateOfferRequest(
        String message,
        Long cost,
        Long deliveryTime
){}
