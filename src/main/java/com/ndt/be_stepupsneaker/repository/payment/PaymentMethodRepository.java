package com.ndt.be_stepupsneaker.repository.payment;

import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository(PaymentMethodRepository.NAME)
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    public static final String NAME = "BasePaymentMethodRepository";
    @Query("SELECT x FROM PaymentMethod x WHERE x.name = :name")
    Optional<PaymentMethod> findByNameMethod(@Param("name") String name);
}
