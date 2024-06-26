package com.ndt.be_stepupsneaker.core.admin.repository.employee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminRoleRequest;
import com.ndt.be_stepupsneaker.entity.employee.Role;
import com.ndt.be_stepupsneaker.repository.employee.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRoleRepository extends RoleRepository {
    @Query("""
    SELECT x FROM Role x 
    WHERE
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE CONCAT('%', :#{#request.q},'%'))
    AND(x.deleted=FALSE)
    """)
    Page<Role> findAllRole (@Param("request")AdminRoleRequest request, Pageable pageable);

    Optional<Role> findByName(String name);

    @Query("""
            SELECT x FROM Role x WHERE (x.name = :name AND :name IN ('SELECT y.name FROM Role y WHERE y.id != :id'))
            """)
    Optional<Role> findByName(@Param("id") String id, @Param("name") String name);
}
