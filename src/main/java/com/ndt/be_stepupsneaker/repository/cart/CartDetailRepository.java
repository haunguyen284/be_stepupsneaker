package com.ndt.be_stepupsneaker.repository.cart;

import com.ndt.be_stepupsneaker.entity.cart.Cart;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(CartDetailRepository.NAME)
public interface CartDetailRepository extends JpaRepository<CartDetail, String> {
    public static final String NAME = "BaseCartDetailRepository";
}
