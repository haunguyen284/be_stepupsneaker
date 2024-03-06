package com.ndt.be_stepupsneaker.core.client.repository.payment;

import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.repository.payment.PaymentMethodRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPaymentMethodRepository extends PaymentMethodRepository {

    Optional<PaymentMethod> findByName(String name);

    @Query("""
            SELECT x FROM PaymentMethod x WHERE (x.name = :name AND :name IN (SELECT y.name FROM PaymentMethod y WHERE y.id != :id))
            """)
    Optional<PaymentMethod> findByName(@Param("id") String id, @Param("name") String name);


}
