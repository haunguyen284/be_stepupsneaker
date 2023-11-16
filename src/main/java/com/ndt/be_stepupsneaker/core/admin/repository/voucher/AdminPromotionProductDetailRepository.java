package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.repository.voucher.PromotionProductDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Primary
@Repository
@Transactional
public interface AdminPromotionProductDetailRepository extends PromotionProductDetailRepository {
    @Modifying
    @Transactional
    @Query("DELETE FROM PromotionProductDetail x WHERE x.promotion.id = :promotion AND x.productDetail.id IN :productDetails")
    void deleteProductDetailsByPromotionId(@Param("promotion") UUID promotion, @Param("productDetails") List<UUID> productDetails);
}
