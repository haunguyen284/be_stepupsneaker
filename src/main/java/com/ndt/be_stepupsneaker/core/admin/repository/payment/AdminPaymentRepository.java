package com.ndt.be_stepupsneaker.core.admin.repository.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.PaymentStatus;
import com.ndt.be_stepupsneaker.repository.payment.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminPaymentRepository extends PaymentRepository {

    @Query("""
                SELECT x FROM Payment x 
                WHERE 
                    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.order.code ILIKE CONCAT('%', :#{#request.q}, '%') 
                    OR 
                    x.order.employee.fullName ILIKE CONCAT('%', :#{#request.q}, '%') 
                    OR
                    x.transactionCode ILIKE CONCAT('%', :#{#request.q}, '%') OR x.order.employee.email ILIKE CONCAT('%', :#{#request.q}, '%')
                    OR
                    x.description ILIKE CONCAT('%', :#{#request.q}, '%') OR x.order.customer.email ILIKE CONCAT('%', :#{#request.q}, '%')
                    OR
                    x.order.customer.fullName ILIKE CONCAT('%', :#{#request.q}, '%') OR x.order.fullName ILIKE CONCAT('%', :#{#request.q}, '%')) 
                    AND
                    (:#{#request.priceMin} IS NULL OR :#{#request.priceMax} IS NULL OR :#{#request.priceMin}  ILIKE '' OR :#{#request.priceMax} ILIKE ''
                    OR 
                    x.totalMoney BETWEEN :#{#request.priceMin} AND :#{#request.priceMax})
                    AND
                    (:#{#request.paymentMethod} IS NULL OR :#{#request.paymentMethod} ILIKE '' OR x.paymentMethod.id = :#{#request.paymentMethod})
                    AND
                    (:status IS NULL OR x.paymentStatus = :status) AND (x.deleted = false)
            """)
    Page<Payment> findAllPayment(@Param("request") AdminPaymentRequest request,
                                 @Param("status") PaymentStatus status, Pageable pageable);


    Optional<Payment> findByTransactionCode(String transactionCode);

    @Query("""
    SELECT x FROM Payment x WHERE (x.transactionCode = :transactionCode AND :transactionCode IN (SELECT y.transactionCode FROM Payment y WHERE y.id != :id))
    """)
    Optional<Payment> findByTransactionCode(@Param("id") String id, @Param("transactionCode") String transactionCode);

}
