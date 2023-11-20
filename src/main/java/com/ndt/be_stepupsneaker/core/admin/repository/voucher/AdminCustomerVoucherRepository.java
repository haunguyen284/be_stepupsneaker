package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.repository.voucher.CustomerVoucherRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Primary
@Repository
@Transactional
public interface AdminCustomerVoucherRepository extends CustomerVoucherRepository {
    @Query("""
            SELECT x FROM CustomerVoucher x 
            WHERE (x.voucher.deleted = FALSE )
            AND (x.customer.deleted =FALSE )
             """)
        // this function not use
    Page<CustomerVoucher> findAllCustomerVoucher(@Param("request") AdminCustomerVoucherRequest request, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM CustomerVoucher x WHERE x.voucher.id = :voucherId AND x.customer.id IN :customerIds")
    void deleteCustomersByVoucherIdAndCustomerIds(@Param("voucherId") String voucherId, @Param("customerIds") List<String> customerIds);

}
