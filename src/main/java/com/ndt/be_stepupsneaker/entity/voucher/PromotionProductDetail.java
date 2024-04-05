package com.ndt.be_stepupsneaker.entity.voucher;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.util.ConvertUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "promotion_product_detail")
@Entity
public class PromotionProductDetail extends PrimaryEntity {
    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Promotion promotion;

    @JoinColumn(name = "product_detail_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDetail productDetail;

}
