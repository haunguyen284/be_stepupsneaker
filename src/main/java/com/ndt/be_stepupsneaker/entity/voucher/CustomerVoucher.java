package com.ndt.be_stepupsneaker.entity.voucher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "customer_voucher")
@Entity
public class CustomerVoucher extends PrimaryEntity {
    @JoinColumn(name = "shop_voucher_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Voucher voucher;

    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;


}
