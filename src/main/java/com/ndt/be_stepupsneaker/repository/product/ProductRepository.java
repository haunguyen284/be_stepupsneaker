package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Material;
import com.ndt.be_stepupsneaker.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(ProductRepository.NAME)

public interface ProductRepository extends JpaRepository<Product, UUID> {
    public static final String NAME = "BaseProductRepository";
}

