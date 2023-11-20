package com.ndt.be_stepupsneaker.repository.voucher;

import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(PromotionRepository.NAME)
public interface PromotionRepository extends JpaRepository<Promotion, String> {
    public static final String NAME = "BasePromotionRepository";
}
