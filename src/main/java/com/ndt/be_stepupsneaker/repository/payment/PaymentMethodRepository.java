package com.ndt.be_stepupsneaker.repository.payment;

import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(PaymentMethodRepository.NAME)
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
    public static final String NAME = "BasePaymentMethodRepository";
}
