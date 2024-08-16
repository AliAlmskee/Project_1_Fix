package com.project1.chatRoom;

import com.project1.profile.ClientProfileMapper;
import com.project1.profile.WorkerProfileMapper;
import com.project1.rate.Rate;
import com.project1.rate.RateCreateDTO;
import com.project1.rate.RateDTO;
import com.project1.user.User;
import com.project1.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
@RequiredArgsConstructor
public abstract class ChatRoomMapper {
    abstract public ChatRoomDto toDTO(ChatRoom chatRoom);
}

