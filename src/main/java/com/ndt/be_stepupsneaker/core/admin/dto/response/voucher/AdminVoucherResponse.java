package com.ndt.be_stepupsneaker.core.admin.dto.response.voucher;

import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
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
public class AdminVoucherResponse {
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

//    List<AdminCustomerVoucherResponse> customerVoucherList;

}
