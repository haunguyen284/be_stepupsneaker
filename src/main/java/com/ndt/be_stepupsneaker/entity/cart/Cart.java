package com.ndt.be_stepupsneaker.entity.cart;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "cart")
@Entity
public class Cart extends PrimaryEntity {
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @OneToMany(mappedBy="cart", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartDetail> cartDetails;
}
