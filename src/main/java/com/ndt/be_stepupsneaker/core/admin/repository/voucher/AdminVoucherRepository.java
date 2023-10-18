package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.repository.product.ColorRepository;
import com.ndt.be_stepupsneaker.repository.voucher.VoucherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminVoucherRepository extends VoucherRepository {
    @Query("""
            SELECT x FROM Voucher x 
            WHERE (:#{#request.name} IS NULL OR :#{#request.name} LIKE '' OR x.name LIKE  CONCAT('%', :#{#request.name}, '%')) 
            AND
            (:#{#request.code} IS NULL OR :#{#request.code} LIKE '' OR x.code LIKE  CONCAT('%', :#{#request.code}, '%'))
             AND
            (:#{#request.status} IS NULL OR x.status = :#{#request.status})
             AND
            (:#{#request.type} IS NULL OR x.type = :#{#request.type})
             AND
            (:#{#request.quantity} = 0 OR x.quantity = :#{#request.quantity})
             AND
            (:#{#request.startDate} IS NULL OR :#{#request.startDate} BETWEEN x.startDate AND x.endDate)
             AND
            (:#{#request.endDate} IS NULL OR :#{#request.endDate} BETWEEN x.startDate AND x.endDate)
             AND
            (x.deleted = false)
             """)
    Page<Voucher> findAllVoucher(@Param("request") AdminVoucherRequest request, Pageable pageable);


    @Query("""
            SELECT x FROM Voucher x 
            WHERE x.code = :code AND :code IN (SELECT y.code FROM Voucher y WHERE y.id != :id)
            """)
    Optional<Voucher> findByCode(@Param("id") UUID id, @Param("code") String code);

    Optional<Voucher> findByCode(String code);



}
