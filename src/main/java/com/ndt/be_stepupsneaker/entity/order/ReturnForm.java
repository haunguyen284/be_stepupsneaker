package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.RefundStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnDeliveryStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormType;
import com.ndt.be_stepupsneaker.util.RandomStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "return_form")
@Entity
public class ReturnForm extends PrimaryEntity {

    @JoinColumn(name = "shop_order_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    @Column(name = "code", length = EntityProperties.LENGTH_CODE)
    private String code;

    @Column(name = "email", length = EntityProperties.LENGTH_EMAIL)
    private String email;

    @Column(name = "amount_to_be_paid")
    private float amountToBePaid;

    @Column(name = "type")
    private ReturnFormType type;

    @Column(name = "payment_type", length = EntityProperties.LENGTH_NAME)
    private String paymentType;

    @Column(name = "payment_info", length = EntityProperties.LENGTH_NAME)
    private String paymentInfo;

    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    @Column(name = "return_delivery_status")
    private ReturnDeliveryStatus returnDeliveryStatus;

    @OneToMany(mappedBy = "returnForm", fetch = FetchType.LAZY)
    private List<ReturnFormDetail> returnFormDetails;

    @OneToMany(mappedBy = "returnForm", fetch = FetchType.LAZY)
    private List<ReturnFormHistory> returnFormHistories;

    @PrePersist
    private void generateCode() {
        int codeLength = 6;
        String randomPart = RandomStringUtil.randomAlphaNumeric(codeLength);
        this.code = "SUS" + "-" + randomPart;
    }

}
