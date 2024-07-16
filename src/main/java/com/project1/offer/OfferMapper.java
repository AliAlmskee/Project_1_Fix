package com.project1.offer;

import com.project1.category.CategoryMapper;
import com.project1.offer.data.*;
import com.project1.profile.ClientProfile;
import com.project1.profile.ClientProfileMapper;
import com.project1.profile.WorkerProfile;
import com.project1.profile.WorkerProfileMapper;
import com.project1.project.data.Project;
import com.project1.skill.SkillMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = WorkerProfileMapper.class)
public abstract class OfferMapper {
    abstract OfferResponse entityToResponse(Offer offer);

    abstract List<OfferResponse> entityToResponse(List<Offer> offer);

    @Mapping(source = "workerId", target = "worker")
    @Mapping(source = "projectId", target = "project")
    abstract Offer toEntity(CreateOfferRequest createOfferRequest);

    WorkerProfile idToWorkerProfile(Long id){
        return WorkerProfile.builder().id(id).build();
    }
    Project idToProject(Long id){
        return Project.builder().id(id).build();
    }



    @Mapping(target = "id", ignore = true)
    public abstract void updateFromDto(@MappingTarget Offer offer, UpdateOfferRequest updateOfferRequest);

}

