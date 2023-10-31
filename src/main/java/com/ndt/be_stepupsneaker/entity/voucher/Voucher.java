package com.ndt.be_stepupsneaker.entity.voucher;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Getter
@Setter
@DynamicUpdate
@Table(name = "shop_voucher")
@Entity
public class Voucher extends PrimaryEntity {
    @Column(name = "code", length = EntityProperties.LENGTH_CODE, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = EntityProperties.LENGTH_NAME, nullable = false)
    @Nationalized
    private String name;

    @Column(name = "status")
    private VoucherStatus status;

    @Column(name = "type")
    private VoucherType type;

    @Column(name = "value")
    private float value;

    @Column(name = "voucher_constraint")
    private float constraint;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(name = "end_date", nullable = false)
    private Long endDate;

    @Column(name = "url_image", columnDefinition = "TEXT")
    @Lob
    private String image;

    @OneToMany(mappedBy = "voucher")
    List<CustomerVoucher> customerVoucherList;
}

