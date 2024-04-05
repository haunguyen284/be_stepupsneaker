package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientCartItemRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminOrderReturnRequest extends PageableRequest {

    private String id;

    private List<AdminCartItemRequest> cartItems;

    private String paymentMethod;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String transactionCode;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String description;

}
