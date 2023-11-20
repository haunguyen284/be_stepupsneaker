package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository(StyleRepository.NAME)

public interface StyleRepository extends JpaRepository<Style, String> {
    public static final String NAME = "BaseStyleRepository";
}