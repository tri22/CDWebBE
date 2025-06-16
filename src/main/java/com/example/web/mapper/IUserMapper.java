package com.example.web.mapper;

import com.example.web.dto.request.UserCreationReq;
import com.example.web.dto.request.UserUpdateReq;
import com.example.web.dto.response.UserResponse;
import com.example.web.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    User toUser(UserCreationReq req);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "role", target = "role")
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateReq req);
}
