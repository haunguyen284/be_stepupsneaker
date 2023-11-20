package com.ndt.be_stepupsneaker.core.admin.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPromotionRequest extends PageableRequest {
    private String id;

    private String code;

    private String name;

    private VoucherStatus status;

    private float value;

    private float constraint;

    private Long startDate;

    private Long endDate;

    private String image;

    private List<String> productDetailIds;
}
