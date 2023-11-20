package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Sole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository(SoleRepository.NAME)

public interface SoleRepository extends JpaRepository<Sole, String> {
    public static final String NAME = "BaseSoleRepository";
}
