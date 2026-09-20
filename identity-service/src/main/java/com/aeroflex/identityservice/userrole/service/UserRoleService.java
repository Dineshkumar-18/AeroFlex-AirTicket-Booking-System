package com.aeroflex.identityservice.userrole.service;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.identityservice.role.entity.Role;
import com.aeroflex.identityservice.role.repository.RoleRepository;
import com.aeroflex.identityservice.user.entity.User;

import java.util.List;

public interface UserRoleService {

    void assignRole(User user, RoleType roleType);

}
