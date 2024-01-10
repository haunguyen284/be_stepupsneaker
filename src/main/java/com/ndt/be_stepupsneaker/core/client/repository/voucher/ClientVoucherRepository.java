package com.ndt.be_stepupsneaker.core.client.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherRequest;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.repository.voucher.BaseUtilRepository;
import com.ndt.be_stepupsneaker.repository.voucher.VoucherRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ClientVoucherRepository extends VoucherRepository, BaseUtilRepository<Voucher> {

    @Query("""
                SELECT x FROM Voucher x 
                WHERE 
                    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE CONCAT('%', :#{#request.q}, '%') OR x.code ILIKE CONCAT('%', :#{#request.q}, '%')) 
                    AND
                    (:status IS NULL OR x.status = :status) 
                    AND
                    (:type IS NULL OR x.type = :type) 
                    AND
                    (:#{#request.quantity} = 0 OR x.quantity = :#{#request.quantity}) 
                    AND
                    (:#{#request.startDate} IS NULL OR :#{#request.startDate} BETWEEN x.startDate AND x.endDate) 
                    AND 
                    (:#{#request.endDate} IS NULL OR :#{#request.endDate} BETWEEN x.startDate AND x.endDate)
                    AND 
                    (:customer IS NULL OR :customer ILIKE '' OR x.id IN (SELECT z.voucher.id FROM CustomerVoucher z WHERE z.customer.id = :customer)) 
                    AND 
                    (x.deleted = false)
            """)
    Page<Voucher> findAllVoucher(@Param("request") ClientVoucherRequest request, Pageable pageable,
                                 @Param("status") VoucherStatus voucherStatus,
                                 @Param("type") VoucherType voucherType,
                                 @Param("customer") String customer);

    @Query("""
            SELECT x FROM Voucher x 
            WHERE x.code = :code AND :code IN (SELECT y.code FROM Voucher y WHERE y.id != :id)
            """)
    Optional<Voucher> findByCode(@Param("id") String id, @Param("code") String code);

    Optional<Voucher> findByCode(String code);


    @Query("""
    SELECT x FROM Voucher x WHERE 
    (
    :customerId IS NULL 
    OR 
    :customerId ILIKE '' 
    OR 
    x.id IN (SELECT z.voucher.id FROM CustomerVoucher z WHERE z.customer.id = :customerId)
    ) 
    AND 
    x.constraint <= :totalMoney 
    ORDER BY x.value 
    """)
    Page<Voucher> findLegitVouchers(Pageable pageable, @Param("customerId") String customerId, @Param("totalMoney") float totalMoney);

}
