package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository(ProductDetailRepository.NAME)

public interface ProductDetailRepository extends JpaRepository<ProductDetail, UUID> {
    public static final String NAME = "BaseProductDetailRepository";
}