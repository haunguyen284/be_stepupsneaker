package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(ColorRepository.NAME)
public interface ColorRepository extends JpaRepository<Color, UUID> {
    public static final String NAME = "BaseColorRepository";
}
