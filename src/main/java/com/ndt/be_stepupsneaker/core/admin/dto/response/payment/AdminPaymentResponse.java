package com.ndt.be_stepupsneaker.core.admin.dto.response.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminPaymentResponse {

    private UUID id;

    private AdminOrderResponse order;

    private AdminPaymentMethodResponse paymentMethod;

    private String transactionCode;

    private float totalMoney;

    private String description;

}
