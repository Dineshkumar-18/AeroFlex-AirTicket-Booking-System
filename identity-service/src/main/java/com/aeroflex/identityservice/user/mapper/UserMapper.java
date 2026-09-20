package com.aeroflex.identityservice.user.mapper;

import com.aeroflex.identityservice.user.dto.response.UserResponse;
import com.aeroflex.identityservice.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

}