package com.aeroflex.identityservice.auth.service.strategy;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.identityservice.auth.enums.OwnerProfileStatus;
import com.aeroflex.identityservice.auth.enums.RegistrationType;
import com.aeroflex.identityservice.role.entity.Role;
import com.aeroflex.identityservice.role.repository.RoleRepository;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.entity.FlightOwnerProfile;
import com.aeroflex.identityservice.user.entity.User;
import com.aeroflex.identityservice.user.repository.FlightOwnerProfileRepository;
import com.aeroflex.identityservice.userrole.service.UserRoleService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FlightOwnerRegistrationStrategy
        implements RegistrationStrategy {

    private final FlightOwnerProfileRepository profileRepository;
    private final UserRoleService userRoleService;

    public FlightOwnerRegistrationStrategy(
            FlightOwnerProfileRepository profileRepository,
            UserRoleService userRoleService
    ) {
        this.profileRepository = profileRepository;
        this.userRoleService = userRoleService;
    }

    @Override
    public RegistrationType supports() {
        return RegistrationType.FLIGHT_OWNER;
    }

    @Override
    @Transactional
    public void handle(
            User user,
            RegisterRequest request
    ) {

       userRoleService.assignRole(user,RoleType.FLIGHT_OWNER);

        FlightOwnerProfile profile =
                new FlightOwnerProfile();

        profile.setUser(user);
        profile.setOwnerProfileStatus(
                OwnerProfileStatus.NOT_COMPLETED
        );

        profileRepository.save(profile);
    }
}
