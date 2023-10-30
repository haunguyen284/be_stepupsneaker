package com.ndt.be_stepupsneaker.core.admin.repository.employee;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.constant.EmployeeStatus;
import com.ndt.be_stepupsneaker.repository.employee.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminEmployeeRepository extends EmployeeRepository {
    @Query("""
                SELECT x FROM Employee x 
                WHERE
                (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.fullName ILIKE CONCAT('%', :#{#request.q}, '%')OR x.email ILIKE CONCAT('%', :#{#request.q}, '%')OR x.address ILIKE CONCAT('%', :#{#request.q}, '%')OR x.gender ILIKE CONCAT('%', :#{#request.q}, '%')OR x.phoneNumber ILIKE CONCAT('%', :#{#request.q}, '%'))
                AND
                (:status IS NULL OR x.status = :status)
                AND(x.deleted=FALSE)
            """)
    Page<Employee> findAllEmployee(@Param("request") AdminEmployeeRequest request, @Param("status") EmployeeStatus status, Pageable pageable);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT x FROM Employee x WHERE (x.phoneNumber = :phoneNumber AND :phoneNumber IN ('SELECT y.phoneNumber FROM Employee y WHERE y.id != :id'))
            """)
    Optional<Employee> findByPhoneNumber(@Param("id") UUID id, @Param("phoneNumber") String phoneNumber);

    @Query("""
            SELECT x FROM Employee x WHERE (x.email = :email AND :email IN ('SELECT y.email FROM Employee y WHERE y.id != :id'))
            """)
    Optional<Employee> findByEmail(@Param("id") UUID id, @Param("email") String email);
}
