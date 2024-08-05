package com.project1.offer;

import com.project1.offer.data.CreateOfferRequest;
import com.project1.offer.data.Offer;
import com.project1.offer.data.OfferResponse;
import com.project1.offer.data.UpdateOfferRequest;
import com.project1.profile.WorkerProfileMapper;
import com.project1.project.data.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = WorkerProfileMapper.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class OfferMapper {
    abstract OfferResponse entityToResponse(Offer offer);

    abstract List<OfferResponse> entityToResponse(List<Offer> offer);

    @Mapping(source = "workerId", target = "worker")
    @Mapping(source = "projectId", target = "project")
    abstract Offer toEntity(CreateOfferRequest createOfferRequest);

    Project idToProject(Long id){
        return Project.builder().id(id).build();
    }



    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(@MappingTarget Offer offer, UpdateOfferRequest updateOfferRequest);

}

