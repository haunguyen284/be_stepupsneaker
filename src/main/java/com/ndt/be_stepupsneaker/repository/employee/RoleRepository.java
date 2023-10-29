package com.ndt.be_stepupsneaker.repository.employee;

import com.ndt.be_stepupsneaker.entity.employee.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository(RoleRepository.NAME)
public interface RoleRepository extends JpaRepository<Role, UUID> {
    public static final String NAME = "BaseRoleRepository";
}
