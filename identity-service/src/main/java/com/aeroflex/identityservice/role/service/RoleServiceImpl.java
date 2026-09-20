package com.aeroflex.identityservice.role.service;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.identityservice.role.entity.Role;
import com.aeroflex.identityservice.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRole(RoleType roleType) {

        return roleRepository.findByName(roleType)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                roleType + " role not found"));
    }
}