package com.ndt.be_stepupsneaker.entity.payment;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Table(name = "payment_method")
@Entity
public class PaymentMethod extends PrimaryEntity {
    @Column(name = "name", nullable = false, unique = true, length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String name;

}
