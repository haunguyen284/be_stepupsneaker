package com.ndt.be_stepupsneaker.core.admin.repository.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.repository.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface AdminCustomerRepository extends CustomerRepository {
    @Query("""
    SELECT x FROM Customer x
    WHERE
    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE ''
     OR x.fullName ILIKE CONCAT('%', :#{#request.q}, '%')
     OR x.email ILIKE CONCAT('%', :#{#request.q}, '%')
     OR x.gender ILIKE CONCAT('%', :#{#request.q}, '%'))
    AND
    (:status IS NULL OR x.status = :status)
    AND
    (:#{#request.dateOfBirth} IS NULL OR x.dateOfBirth = :#{#request.dateOfBirth})
    AND
    (x.deleted = FALSE)
    AND
    (x.customerAddresses is empty OR x.customerAddresses is not empty AND EXISTS (SELECT a FROM Address a WHERE a.customer = x AND a.isDefault = TRUE))
    """)
    Page<Customer> findAllCustomer(@Param("request") AdminCustomerRequest request, @Param("status")CustomerStatus status, Pageable pageable);


    Optional<Customer> findByEmail(String email);

    @Query("""
            SELECT x FROM Customer x WHERE (x.email = :email AND :email IN ('SELECT y.email FROM Customer y WHERE y.id != :id'))
            """)
    Optional<Customer> findByEmail(@Param("id") UUID id, @Param("email") String email);

}
