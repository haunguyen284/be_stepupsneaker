package com.ndt.be_stepupsneaker.repository.payment;

import com.ndt.be_stepupsneaker.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(PaymentRepository.NAME)
public interface PaymentRepository extends JpaRepository<Payment, String> {
    public static final String NAME = "BasePaymentRepository";
}
