package com.ndt.be_stepupsneaker.core.client.dto.response.voucher;

import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientPromotionNoPromotionProductDetailResponse {
    private String id;

    private String code;

    private String name;

    private VoucherStatus status;

    private float value;

    private float constraint;

    private Long startDate;

    private Long endDate;

    private String image;
}
