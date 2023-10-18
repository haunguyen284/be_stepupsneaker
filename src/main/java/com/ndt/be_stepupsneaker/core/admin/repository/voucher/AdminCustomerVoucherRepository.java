package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.repository.voucher.CustomerVoucherRepository;
import com.ndt.be_stepupsneaker.repository.voucher.VoucherRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Repository
public interface AdminCustomerVoucherRepository extends CustomerVoucherRepository {
    @Query("""
            SELECT x FROM CustomerVoucher x 
            WHERE (x.voucher.deleted = FALSE )
            AND (x.customer.deleted =FALSE )
             """)
    Page<CustomerVoucher> findAllCustomerVoucher(@Param("request") AdminCustomerVoucherRequest request, Pageable pageable);

    @Query("""
            SELECT x.customer FROM CustomerVoucher x 
            WHERE x.voucher.id  = :voucherId 
            AND
            (:#{#request.fullName} IS NULL OR :#{#request.fullName} LIKE '' OR x.customer.fullName LIKE  CONCAT('%', :#{#request.fullName}, '%'))
            AND
            (:#{#request.email} IS NULL OR :#{#request.email} LIKE '' OR x.customer.email LIKE  CONCAT('%', :#{#request.email}, '%'))
            AND
            (:#{#request.gender} IS NULL OR :#{#request.gender} LIKE '' OR x.customer.gender LIKE  CONCAT('%', :#{#request.gender}, '%'))
            AND
            (:status IS NULL OR  x.customer.status = :status)
            AND
            (:#{#request.dateOfBirth} IS NULL OR x.customer.dateOfBirth = :#{#request.dateOfBirth})
            AND
            (x.deleted = FALSE)
             """)
    Page<Customer> getAllCustomerByVoucherId(@Param("voucherId") UUID voucherId, @Param("status") CustomerStatus status, @Param("request") AdminCustomerRequest request, Pageable pageable);

    @Query("""
            SELECT x FROM Customer x 
            WHERE x.id NOT IN (SELECT y.id FROM CustomerVoucher y WHERE y.voucher.id = :voucherId)
            AND
            (:#{#request.fullName} IS NULL OR :#{#request.fullName} LIKE '' OR x.fullName LIKE  CONCAT('%', :#{#request.fullName}, '%'))
            AND
            (:#{#request.email} IS NULL OR :#{#request.email} LIKE '' OR x.email LIKE  CONCAT('%', :#{#request.email}, '%'))
            AND
            (:#{#request.gender} IS NULL OR :#{#request.gender} LIKE '' OR x.gender LIKE  CONCAT('%', :#{#request.gender}, '%'))
            AND
            (:status IS NULL OR  x.status = :status)
            AND
            (:#{#request.dateOfBirth} IS NULL OR x.dateOfBirth = :#{#request.dateOfBirth})
            AND
            (x.deleted = FALSE)
             """)
    Page<Customer> getAllCustomerNotInVoucherId(@Param("voucherId") UUID voucherId, @Param("status") CustomerStatus status, @Param("request") AdminCustomerRequest request, Pageable pageable);


}
