package com.project1.rate;

public record RateCreateDTO(
    String description,
    Double overall,
    Double proficiency,
    Double communication,
    Double quality,
    RatedType rated,
    Long clientProfileId,
    Long workerProfileId
){
    public double totalRate(){
        return (overall+proficiency+communication+quality)/4;
    }
}
