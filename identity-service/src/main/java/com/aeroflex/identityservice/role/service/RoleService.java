package com.aeroflex.identityservice.role.service;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.identityservice.role.entity.Role;

public interface RoleService {
    Role getRole(RoleType roleType);
}
