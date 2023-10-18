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
    (:#{#request.fullName} IS NULL OR :#{#request.fullName} LIKE '' OR x.fullName LIKE CONCAT('%', :#{#request.fullName}, '%'))
    AND
    (:#{#request.email} IS NULL OR :#{#request.email} LIKE '' OR x.email LIKE CONCAT('%', :#{#request.email}, '%'))
    AND
    (:#{#request.address} IS NULL OR :#{#request.address} LIKE '' OR x.address LIKE CONCAT('%', :#{#request.address}, '%'))
    AND
    (:#{#request.gender} IS NULL OR x.gender = :#{#request.gender})
    AND
    (:#{#request.phoneNumber} IS NULL OR :#{#request.phoneNumber} LIKE '' OR x.phoneNumber LIKE CONCAT('%', :#{#request.phoneNumber}, '%'))
    AND
    (:status IS NULL OR x.status = :status)
""")

    Page<Employee> findAllEmployee(@Param("request")AdminEmployeeRequest request, @Param("status")EmployeeStatus status, Pageable pageable);

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT x FROM Employee x WHERE (x.phoneNumber = :phoneNumber AND :phoneNumber IN ('SELECT y.phoneNumber FROM Employee y WHERE y.id != :id'))
            """)
     Optional<Employee> findByPhoneNumber(@Param("id")UUID id, @Param("phoneNumber") String phoneNumber);

    @Query("""
            SELECT x FROM Employee x WHERE (x.email = :email AND :email IN ('SELECT y.email FROM Employee y WHERE y.id != :id'))
            """)
    Optional<Employee> findByEmail(@Param("id")UUID id, @Param("email") String email);
}
