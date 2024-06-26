package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(BrandRepository.NAME)

public interface BrandRepository extends JpaRepository<Brand, String> {
    public static final String NAME = "BaseBrandRepository";
}
