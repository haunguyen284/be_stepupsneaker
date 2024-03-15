package com.ndt.be_stepupsneaker.core.admin.dto.request.voucher;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminVoucherRequest extends PageableRequest {
    private String id;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String code;

    @JsonDeserialize(using = CustomStringDeserializer.class)
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
