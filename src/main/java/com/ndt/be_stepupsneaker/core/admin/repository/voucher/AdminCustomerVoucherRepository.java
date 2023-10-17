package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.repository.voucher.CustomerVoucherRepository;
import com.ndt.be_stepupsneaker.repository.voucher.VoucherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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


//    @Query("""
//            SELECT x FROM Voucher x WHERE x.code = :code AND :code IN (SELECT y.code FROM Voucher y WHERE y.id != :id)
//            """)
//    Optional<Voucher> findByCode(@Param("id") UUID id, @Param("code") String code);

    Optional<CustomerVoucher> findByCode(String code);
}
