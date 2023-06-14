package com.pws.usermanagement.repo;

import com.pws.usermanagement.entity.Role;
import com.pws.usermanagement.entity.UserRoleXref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRoleXrefRepository extends JpaRepository<UserRoleXref, UUID> {
    List<UserRoleXref> findByUserId(UUID id);
    @Query("select o.role from UserRoleXref o where o.user.id=:id and role.isActive=TRUE")
    List<Role> findUserRoleByUserId(UUID id);
}
