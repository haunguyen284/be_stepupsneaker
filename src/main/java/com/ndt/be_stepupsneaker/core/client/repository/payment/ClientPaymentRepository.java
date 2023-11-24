package com.ndt.be_stepupsneaker.core.client.repository.payment;

import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.repository.payment.PaymentRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPaymentRepository extends PaymentRepository {

    Optional<Payment> findByTransactionCode(String transactionCode);

    @Query("""
    SELECT x FROM Payment x WHERE (x.transactionCode = :transactionCode AND :transactionCode IN (SELECT y.transactionCode FROM Payment y WHERE y.id != :id))
    """)
    Optional<Payment> findByTransactionCode(@Param("id") String id, @Param("transactionCode") String transactionCode);
    
}
