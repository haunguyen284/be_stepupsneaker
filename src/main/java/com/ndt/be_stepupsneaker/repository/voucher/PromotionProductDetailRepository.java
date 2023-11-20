package com.ndt.be_stepupsneaker.repository.voucher;

import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(PromotionProductDetailRepository.NAME)
public interface PromotionProductDetailRepository extends JpaRepository<PromotionProductDetail, String> {
    public static final String NAME = "BasePromotionProductDetailRepository";
}
