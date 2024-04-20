package com.ndt.be_stepupsneaker.core.admin.dto.request.payment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.PaymentStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminPaymentRequest extends PageableRequest {

    private String id;

    @NotBlank(message = "{payment.order.not_blank}")
    private String order;

    @NotBlank(message = "{payment.payment_method.not_blank}")
    private String paymentMethod;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String transactionCode;

    @NotNull(message = "{payment.total_money.not_null}")
    private float totalMoney;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String description;

//    filter
    private String priceMin;

    private String priceMax;

    private PaymentStatus status;

}
