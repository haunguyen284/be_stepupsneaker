package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.repository.voucher.VoucherRepository;
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
public interface AdminVoucherRepository extends VoucherRepository {
    @Query("""
            SELECT x FROM Voucher x LEFT JOIN x.customerVoucherList y
            WHERE (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%')OR x.code ILIKE  CONCAT('%', :#{#request.q}, '%')) 
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
            (CAST(:customerId AS java.util.UUID) IS NULL OR CAST(:customerId AS java.util.UUID)  = y.customer.id)
             AND
            (CAST(:noCustomerId AS java.util.UUID) IS NULL OR x.id NOT IN (SELECT z.voucher FROM CustomerVoucher z WHERE z.customer.id = CAST(:noCustomerId AS java.util.UUID) ))
             AND
            (x.deleted = false)
             """)
    Page<Voucher> findAllVoucher(@Param("request") AdminVoucherRequest request, Pageable pageable,
                                 @Param("status") VoucherStatus voucherStatus,
                                 @Param("type") VoucherType voucherType,
                                 @Param("customerId") UUID customerId,
                                 @Param("noCustomerId") UUID noCustomerId
                                 );


    @Query("""
            SELECT x FROM Voucher x 
            WHERE x.code = :code AND :code IN (SELECT y.code FROM Voucher y WHERE y.id != :id)
            """)
    Optional<Voucher> findByCode(@Param("id") UUID id, @Param("code") String code);

    Optional<Voucher> findByCode(String code);


}
