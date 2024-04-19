package com.ndt.be_stepupsneaker.core.admin.dto.request.voucher;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
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

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String code;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String name;

    private VoucherStatus status;

    @Max(value = 70,message = "{promotion.value.max}")
    private float value;

    private float constraint;

    private Long startDate;

    private Long endDate;

    private String image;

    private List<String> productDetailIds;

//    filter
    private String priceMin;

    private String priceMax;

}
