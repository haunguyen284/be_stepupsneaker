package com.ndt.be_stepupsneaker.core.admin.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import jakarta.persistence.Column;
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

public class AdminVoucherRequest extends PageableRequest {
    private UUID id;

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

    List<AdminCustomerVoucherRequest> adminCustomerVoucherRequests;
}
