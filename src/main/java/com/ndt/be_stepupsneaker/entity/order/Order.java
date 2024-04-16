package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.review.Review;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.util.RandomStringUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.List;

@Getter
@Setter
@Table(name = "shop_order")
@Entity
@Audited
public class Order extends PrimaryEntity {

    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotAudited
    private Customer customer;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotAudited
    private Employee employee;

    @JoinColumn(name = "shop_voucher_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Voucher voucher;

    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    @Column(name = "phone_number", length = EntityProperties.LENGTH_PHONE)
    private String phoneNumber;

    @Column(name = "full_name", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String fullName;

    @Column(name = "email", length = EntityProperties.LENGTH_EMAIL)
    private String email;

    @Column(name = "origin_money")
    private float originMoney;

    @Column(name = "reducel_money")
    private float reduceMoney;

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
    @NotAudited
    private OrderType type;

    @Column(name = "note", length = EntityProperties.LENGTH_DESCRIPTION)
    @Nationalized
    private String note;

    @Column(name = "versionUpdate")
    private Integer versionUpdate = 0;

    @Column(name = "status")
    private OrderStatus status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @NotAudited
    private List<Review> reviews;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @NotAudited
    private List<OrderHistory> orderHistories;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @NotAudited
    private List<Payment> payments;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @NotAudited
    private List<VoucherHistory> voucherHistories;

    @Column(name = "code", updatable = false, length = EntityProperties.LENGTH_CODE, unique = true)
    @NotAudited
    private String code;

    @PrePersist
    private void generateCode() {
        int codeLength = 5;
        String randomPart = RandomStringUtil.randomAlphaNumeric(codeLength);
        this.code = "SUS" + "-" + randomPart;
        this.versionUpdate = 1;
    }


}
