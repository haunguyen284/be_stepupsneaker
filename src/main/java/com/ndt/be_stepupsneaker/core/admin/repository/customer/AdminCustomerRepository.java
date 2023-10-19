package com.ndt.be_stepupsneaker.core.admin.repository.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.repository.customer.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminCustomerRepository extends CustomerRepository {
    @Query("""
    SELECT x FROM Customer x 
    WHERE
    (:#{#request.fullName} IS NULL OR :#{#request.fullName} LIKE '' OR x.fullName LIKE CONCAT('%', :#{#request.fullName}, '%'))
    AND
    (:#{#request.email} IS NULL OR :#{#request.email} LIKE '' OR x.email LIKE CONCAT('%', :#{#request.email}, '%'))
    AND
    (:status IS NULL OR x.status = :status)
    AND
    (:#{#request.gender} IS NULL OR x.gender = :#{#request.gender})
     AND
    (:#{#request.dateOfBirth} IS NULL OR x.dateOfBirth = :#{#request.dateOfBirth})
    """)
    Page<Customer> findAllCustomer(@Param("request") AdminCustomerRequest request, @Param("status")CustomerStatus status, Pageable pageable);


    Optional<Customer> findByEmail(String email);

    @Query("""
            SELECT x FROM Customer x WHERE (x.email = :email AND :email IN ('SELECT y.email FROM Customer y WHERE y.id != :id'))
            """)
    Optional<Customer> findByEmail(@Param("id") UUID id, @Param("email") String email);


}