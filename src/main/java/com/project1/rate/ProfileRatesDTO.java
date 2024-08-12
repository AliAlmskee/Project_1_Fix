package com.project1.rate;

import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public record ProfileRatesDTO(
        List<RateDTO> rates,
        Integer count,
        Double avg,
        Integer count5,
        Integer count4,
        Integer count3,
        Integer count2,
        Integer count1

){
}
