package com.aeroflex.identityservice.role.repository;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.identityservice.role.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    @Query("""
        SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END
        FROM UserRole ur
        WHERE ur.user.id = :userId
          AND ur.role.name = :roleName
        """)
    boolean existsByUserIdAndRoleName(
            @Param("userId") UUID userId,
            @Param("roleName") RoleType roleName
    );

}
