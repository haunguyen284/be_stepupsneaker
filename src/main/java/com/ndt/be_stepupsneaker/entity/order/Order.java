package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Table(name = "shop_order")
@Entity
public class Order extends PrimaryEntity {
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @JoinColumn(name = "shop_voucher_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Voucher voucher;

    @Column(name = "phone_number", length = EntityProperties.LENGTH_PHONE)
    private String phoneNumber;

    @Column(name = "address", length = EntityProperties.LENGTH_ADDRESS)
    @Nationalized
    private String address;

    @Column(name = "full_name", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String fullName;

    @Column(name = "total_money")
    private float totalMoney;

    @Column(name = "shipping_money")
    private float shippingMoney;

    @Column(name = "confirmation_date")
    private Long confirmationDate;

    @Column(name = "expected_delivery_date")
    private Long expectedDeliveryDate;

    @Column(name = "delivery_start_date")
    private Long deliveryStartDate;

    @Column(name = "received_date")
    private Long receivedDate;

    @Column(name = "type")
    private OrderType type;

    @Column(name = "note", length = EntityProperties.LENGTH_DESCRIPTION)
    @Nationalized
    private String note;

    @Column(name = "status")
    private OrderStatus status;

}
