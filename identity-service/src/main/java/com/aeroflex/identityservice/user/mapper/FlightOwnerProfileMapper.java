package com.aeroflex.identityservice.user.mapper;

import com.aeroflex.identityservice.user.dto.request.FlightOwnerProfileRequest;
import com.aeroflex.identityservice.user.dto.response.FlightOwnerProfileResponse;
import com.aeroflex.identityservice.user.entity.FlightOwnerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FlightOwnerProfileMapper {

    FlightOwnerProfileResponse toResponse(
            FlightOwnerProfile profile
    );

    void updateEntity(
            FlightOwnerProfileRequest request,
            @MappingTarget FlightOwnerProfile profile
    );
}
