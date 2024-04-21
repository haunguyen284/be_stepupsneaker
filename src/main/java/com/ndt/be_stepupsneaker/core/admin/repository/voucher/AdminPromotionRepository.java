package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionRequest;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.repository.voucher.BaseUtilRepository;
import com.ndt.be_stepupsneaker.repository.voucher.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface AdminPromotionRepository extends PromotionRepository, BaseUtilRepository<Promotion> {

    @Query("""
                SELECT x FROM Promotion x
                WHERE
                    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE CONCAT('%', :#{#request.q}, '%') OR x.code ILIKE CONCAT('%', :#{#request.q}, '%'))
                    AND
                    (:#{#request.startDate} IS NULL OR :#{#request.endDate} IS NULL OR (x.startDate >= :#{#request.startDate} AND x.endDate <= :#{#request.endDate}))
                    AND            
                    (:#{#request.priceMin} IS NULL OR :#{#request.priceMin} ILIKE ''  OR :#{#request.priceMax} IS NULL
                    OR 
                    :#{#request.priceMax} ILIKE '' OR x.value BETWEEN :#{#request.priceMin} AND :#{#request.priceMax})
                    AND
                    (:productDetail IS NULL OR :productDetail ILIKE '' OR x.id IN (SELECT y.promotion.id FROM PromotionProductDetail y WHERE y.productDetail.id = :productDetail))
                    AND
                    (:noProductDetail IS NULL OR :noProductDetail ILIKE '' OR  x.id NOT IN (SELECT y.promotion.id FROM PromotionProductDetail y WHERE y.productDetail.id = :noProductDetail))
                    AND
                    (x.deleted = false) AND (:status IS NULL OR x.status = :status)
            """)
    Page<Promotion> findAllPromotion(@Param("request") AdminPromotionRequest request, Pageable pageable,
                                     @Param("status") VoucherStatus status,
                                     @Param("productDetail") String productDetail,
                                     @Param("noProductDetail") String noProductDetail);


    @Query("""
            SELECT x FROM Promotion x 
            WHERE x.code = :code AND :code IN (SELECT y.code FROM Promotion y WHERE y.id != :id)
            """)
    Optional<Promotion> findByCode(@Param("id") String id, @Param("code") String code);

    Optional<Promotion> findByCode(String code);


}
