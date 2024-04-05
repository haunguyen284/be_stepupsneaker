package com.ndt.be_stepupsneaker.entity.payment;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Table(name = "payment")
@Entity
public class Payment extends PrimaryEntity {
    @JoinColumn(name = "shop_order_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentMethod paymentMethod;

    @Column(name = "transaction_code", nullable = false, length = EntityProperties.LENGTH_NAME_SHORT)
    private String transactionCode;

    @Column(name = "total_money")
    private float totalMoney;

    @Column(name = "description", length = EntityProperties.LENGTH_DESCRIPTION)
    @Nationalized
    private String description;

    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
}