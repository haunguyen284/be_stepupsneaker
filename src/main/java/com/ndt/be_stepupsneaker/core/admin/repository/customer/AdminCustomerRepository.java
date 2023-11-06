package com.ndt.be_stepupsneaker.core.admin.repository.customer;

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
            SELECT x FROM Customer x LEFT JOIN x.addressList y LEFT JOIN x.customerVoucherList z
            WHERE
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE ''
             OR x.fullName ILIKE CONCAT('%', :#{#request.q}, '%')
             OR x.email ILIKE CONCAT('%', :#{#request.q}, '%')
             OR x.gender ILIKE CONCAT('%', :#{#request.q}, '%')
             OR y.wardName ILIKE CONCAT('%', :#{#request.q}, '%')
             OR y.districtName ILIKE CONCAT('%', :#{#request.q}, '%')
             OR y.provinceName ILIKE CONCAT('%', :#{#request.q}, '%')
             OR y.more ILIKE CONCAT('%', :#{#request.q}, '%')
             OR y.phoneNumber ILIKE CONCAT('%', :#{#request.q}, '%'))
            AND
            (:status IS NULL OR x.status = :status)
            AND
            (:#{#request.dateOfBirth} IS NULL OR x.dateOfBirth = :#{#request.dateOfBirth})
            AND
            (CAST(:voucherId as java.util.UUID) IS NULL  OR  z.voucher.id = CAST(:voucherId AS java.util.UUID))
            AND
             (CAST(:noVoucherId as java.util.UUID) IS NULL  OR x.id 
             NOT IN (SELECT o.customer.id FROM CustomerVoucher o WHERE o.voucher.id = CAST(:noVoucherId AS java.util.UUID) ))
            AND
            (x.deleted = FALSE)
            AND
            (x.addressList IS EMPTY OR x.addressList  IS NOT EMPTY AND EXISTS (SELECT a FROM Address a WHERE a.customer = x AND a.isDefault = TRUE))
            """)
    Page<Customer> findAllCustomer(@Param("request") AdminCustomerRequest request,
                                   @Param("voucherId") UUID voucherId,
                                   @Param("noVoucherId") UUID noVoucherId,
                                   @Param("status") CustomerStatus status, Pageable pageable);

    Optional<Customer> findByEmail(String email);

    @Query("""
            SELECT x FROM Customer x WHERE (x.email = :email AND :email IN ('SELECT y.email FROM Customer y WHERE y.id != :id'))
            """)
    Optional<Customer> findByEmail(@Param("id") UUID id, @Param("email") String email);

}
