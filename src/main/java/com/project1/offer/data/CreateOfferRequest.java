package com.project1.offer.data;

import jakarta.validation.constraints.NotNull;

public record CreateOfferRequest(
        @NotNull
        String message,
        @NotNull
        Long cost,
        @NotNull
        Long deliveryTime,
        @NotNull
        Long workerId,
        @NotNull
        Long projectId
){}
