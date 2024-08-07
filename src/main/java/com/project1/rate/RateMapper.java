package com.project1.rate;

import com.project1.profile.ClientProfileMapper;
import com.project1.profile.WorkerProfileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {WorkerProfileMapper.class, ClientProfileMapper.class})
public abstract class RateMapper {
    @Mapping(source = "clientProfileId", target = "client")
    @Mapping(source = "workerProfileId", target = "worker")
    abstract Rate dtoToEntity(RateDTO dto);
    @Mapping(source = "clientProfileId", target = "client")
    @Mapping(source = "workerProfileId", target = "worker")
    abstract Rate createEntity(RateCreateDTO dto);
}

