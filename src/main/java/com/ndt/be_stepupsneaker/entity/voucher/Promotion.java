package com.ndt.be_stepupsneaker.entity.voucher;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Getter
@Setter
@DynamicUpdate
@Table(name = "promotion")
@Entity
public class Promotion extends PrimaryEntity {
    @Column(name = "code", length = EntityProperties.LENGTH_CODE, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = EntityProperties.LENGTH_NAME, nullable = false)
    @Nationalized
    private String name;

    @Column(name = "status")
    private VoucherStatus status;

    @Column(name = "value")
    private float value;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(name = "end_date", nullable = false)
    private Long endDate;

    @Column(name = "url_image")
    private String image;

    @OneToMany(mappedBy = "promotion")
    List<PromotionProductDetail> promotionProductDetailList;
}
