package com.ndt.be_stepupsneaker.core.client.dto.request.payment;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientPaymentRequest extends PageableRequest {

    private String id;

    @NotBlank(message = "Order must be not null")
    private String order;

    @NotBlank(message = "Payment Method must be not null")
    private String paymentMethod;

    @NotBlank(message = "Transaction code Method must be not null")
    private String transactionCode;

    @NotNull(message = "Total money must be not null")
    private float totalMoney;

    private String description;

}
