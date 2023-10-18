package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
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
            WHERE (:#{#request.voucher.name} IS NULL OR :#{#request.name} LIKE '' OR x.voucher.name LIKE  CONCAT('%', :#{#request.voucher.name}, '%')) 
            AND
            (:#{#request.voucher.code} IS NULL OR :#{#request.voucher.code} LIKE '' OR x.voucher.code LIKE  CONCAT('%', :#{#request.voucher.name}, '%'))
            AND
            (:#{#request.customer.fullName} IS NULL OR x.customer.fullName = :#{#request.customer.fullName})
            AND
            (x.voucher.deleted = FALSE )
             """)
    Page<CustomerVoucher> findAllCustomerVoucher(@Param("request") AdminCustomerVoucherRequest request, Pageable pageable);


    @Query("""
            SELECT x.customer FROM CustomerVoucher x 
            WHERE (x.voucher.id  = :idVoucher) 
            AND
            (:#{#request.customerRequest.fullName} IS NULL OR :#{#request.customerRequest.fullName} LIKE '' OR x.customer.fullName LIKE  CONCAT('%', :#{#request.customerRequest.fullName}, '%'))
            AND
            (:#{#request.customerRequest.email} IS NULL OR :#{#request.customerRequest.email} LIKE '' OR x.customer.email LIKE  CONCAT('%', :#{#request.customerRequest.email}, '%'))
            AND
            (:#{#request.customerRequest.gender} IS NULL OR :#{#request.customerRequest.gender} LIKE '' OR x.customer.gender LIKE  CONCAT('%', :#{#request.customerRequest.gender}, '%'))
            AND
            (x.deleted = FALSE)
             """)
    Page<Customer> getAllCustomerByVoucherId(@Param("idVoucher")UUID idVoucher,@Param("request") AdminCustomerVoucherRequest request, Pageable pageable);


//    List<CustomerVoucher> findCustomerVouchersByVoucherIdAndCustomerIds(UUID voucherId, List<UUID> customerIds);
}
