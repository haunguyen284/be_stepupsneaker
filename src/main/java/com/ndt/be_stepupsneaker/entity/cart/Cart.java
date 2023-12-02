package com.ndt.be_stepupsneaker.entity.cart;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "cart")
@Entity
public class Cart extends PrimaryEntity {
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Customer customer;


}
