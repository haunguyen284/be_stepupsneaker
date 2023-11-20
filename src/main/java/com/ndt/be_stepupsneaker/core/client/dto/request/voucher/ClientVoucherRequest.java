package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClientVoucherRequest extends PageableRequest {
    private String id;

    private String code;

    private String name;

    private VoucherStatus status;

    private VoucherType type;

    private float value;

    private float constraint;

    private int quantity;

    private Long startDate;

    private Long endDate;

    private String image;


}
