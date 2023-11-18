package com.ndt.be_stepupsneaker.core.client.dto.response.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderNoOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderNoOrderDetailResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClientPaymentResponse {

    private UUID id;

    private ClientOrderNoOrderDetailResponse order;

    private ClientPaymentMethodResponse paymentMethod;

    private String transactionCode;

    private float totalMoney;

    private String description;

}
