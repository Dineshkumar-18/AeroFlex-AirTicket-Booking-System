package com.aeroflex.identityservice.userrole.service;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.identityservice.role.entity.Role;
import com.aeroflex.identityservice.role.entity.UserRole;
import com.aeroflex.identityservice.role.repository.RoleRepository;
import com.aeroflex.identityservice.role.repository.UserRoleRepository;
import com.aeroflex.identityservice.role.service.RoleService;
import com.aeroflex.identityservice.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    @Autowired
    public UserRoleServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void assignRole(User user, RoleType roleType) {

        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + roleType.name()
                        ));


        if (userRoleRepository.existsByUserIdAndRoleName(user.getId(), role.getName())) {
            logger.error("Role already assigned: {} to user: {}", roleType.name(), user.getId());
            return;
        }

        UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(role)
                    .build();

        user.getUserRoles().add(userRole);
    }
}
