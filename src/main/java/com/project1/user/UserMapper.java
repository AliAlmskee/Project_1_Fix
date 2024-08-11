package com.project1.user;


import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
    List<UserDTO> usersToUserDTOs(List<User> users);
    User toEntity(UserDTO userDTO);
}
