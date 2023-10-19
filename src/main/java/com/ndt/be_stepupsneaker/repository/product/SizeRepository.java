package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.entity.product.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(SizeRepository.NAME)
public interface SizeRepository extends JpaRepository<Size, UUID> {
    public static final String NAME = "BaseSizeRepository";
}