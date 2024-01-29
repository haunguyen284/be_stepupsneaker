package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Code must be not null")
    private String code;

    @NotBlank(message = "Name must be not null")
    private String name;

    private VoucherStatus status;

    @NotNull(message = "Type must be not null")
    private VoucherType type;

    @NotNull(message = "Value must be not null")
    private float value;

    @NotNull(message = "Constraint must be not null")
    private float constraint;

    @NotNull(message = "Quantity must be not null")
    private int quantity;

    @NotNull(message = "StartDate must be not null")
    private Long startDate;

    @NotNull(message = "EndDate must be not null")
    private Long endDate;

    private String image;


}
