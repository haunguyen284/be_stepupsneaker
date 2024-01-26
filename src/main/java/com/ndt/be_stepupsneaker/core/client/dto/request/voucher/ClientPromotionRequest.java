package com.ndt.be_stepupsneaker.core.client.dto.request.voucher;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientPromotionRequest extends PageableRequest {
    private String id;

    @NotBlank(message = "Code must be not null")
    private String code;

    @NotBlank(message = "NamePromotion must be not null")
    private String name;

    @NotNull(message = "Status must be not null")
    private VoucherStatus status;

    @NotNull(message = "Value must be not null")
    private float value;

    @NotNull(message = "Constraint must be not null")
    private float constraint;

    @NotNull(message = "StartDate must be not null")
    private Long startDate;

    @NotNull(message = "EndDate must be not null")
    private Long endDate;

    private String image;

    private List<String> productDetailIds;
}
