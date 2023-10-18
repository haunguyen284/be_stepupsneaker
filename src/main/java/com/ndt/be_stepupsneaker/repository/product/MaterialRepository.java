package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.entity.product.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository(MaterialRepository.NAME)

public interface MaterialRepository extends JpaRepository<Material, UUID> {
    public static final String NAME = "BaseMaterialRepository";
}
