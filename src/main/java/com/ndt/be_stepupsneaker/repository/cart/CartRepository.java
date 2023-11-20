package com.ndt.be_stepupsneaker.repository.cart;

import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(CartRepository.NAME)
public interface CartRepository extends JpaRepository<Cart, String> {
    public static final String NAME = "BaseCartRepository";
}
