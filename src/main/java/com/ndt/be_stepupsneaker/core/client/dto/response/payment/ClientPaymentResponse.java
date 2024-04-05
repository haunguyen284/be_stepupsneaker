package com.ndt.be_stepupsneaker.core.client.dto.response.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderNoOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderNoOrderDetailResponse;
import com.ndt.be_stepupsneaker.infrastructure.constant.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientPaymentResponse {

    private String id;

    private ClientOrderNoOrderDetailResponse order;

    private ClientPaymentMethodResponse paymentMethod;

    private String transactionCode;

    private float totalMoney;

    private PaymentStatus paymentStatus;

    private String description;

    private Long createdAt;

}
