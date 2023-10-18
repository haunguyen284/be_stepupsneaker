package com.ndt.be_stepupsneaker.core.admin.repository.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
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
            (:#{#request.status} IS NULL OR x.status = :#{#request.status})
            AND
            (:#{#request.gender} IS NULL OR x.gender = :#{#request.gender})
            """)
    Page<Customer> findAllCustomer(@Param("request") AdminCustomerRequest request, Pageable pageable);

    Optional<Customer> findByFullName(String fullName);
    Optional<Customer> findByEmail(String email);

    @Query("""
    SELECT x FROM Customer x WHERE (x.fullName = :fullName AND :fullName IN (SELECT y.fullName FROM Customer y WHERE y.id != :id))
    """)
    Optional<Customer> findByFullName(@Param("id")UUID id, @Param("fullName") String fullName);


}
