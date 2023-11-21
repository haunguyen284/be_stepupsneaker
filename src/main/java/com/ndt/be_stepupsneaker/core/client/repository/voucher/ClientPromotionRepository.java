package com.ndt.be_stepupsneaker.core.client.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientPromotionRequest;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.repository.voucher.BaseUtilRepository;
import com.ndt.be_stepupsneaker.repository.voucher.PromotionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPromotionRepository extends PromotionRepository, BaseUtilRepository<Promotion> {

    @Query("""
                SELECT x FROM Promotion x
                WHERE
                    (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE CONCAT('%', :#{#request.q}, '%') OR x.code ILIKE CONCAT('%', :#{#request.q}, '%'))
                    AND
                    (:#{#request.startDate} IS NULL OR :#{#request.startDate} BETWEEN x.startDate AND x.endDate)
                    AND
                    (:#{#request.endDate} IS NULL OR :#{#request.endDate} BETWEEN x.startDate AND x.endDate)
                    AND
                    (:productDetail IS NULL OR :productDetail ILIKE '' OR x.id IN (SELECT y.promotion.id FROM PromotionProductDetail y WHERE y.productDetail.id = :productDetail))
                    AND
                    (:noProductDetail IS NULL OR :noProductDetail ILIKE '' OR  x.id NOT IN (SELECT y.promotion.id FROM PromotionProductDetail y WHERE y.productDetail.id = :noProductDetail))
                    AND
                    (x.deleted = false) AND (:status IS NULL OR x.status = :status)
            """)
    Page<Promotion> findAllPromotion(@Param("request") ClientPromotionRequest request, Pageable pageable,
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
