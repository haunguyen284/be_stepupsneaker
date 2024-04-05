package com.ndt.be_stepupsneaker.core.client.dto.request.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.util.CustomStringDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClientOrderRequest extends PageableRequest {

    private String id;

    private String customer;

    private String employee;

    private String voucher;

    private ClientAddressRequest addressShipping;

    private List<ClientCartItemRequest> cartItems;

    @NotBlank(message = "Email must be not null")
    private String phoneNumber;

    @NotBlank(message = "Email must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String fullName;

    @NotBlank(message = "Email must be not null")
    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String email;

    @NotNull(message = "Email must be not null")
    private float shippingMoney;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String note;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String orderHistoryNote;

    private TransactionInfo transactionInfo;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String paymentMethod;

    private OrderStatus status;

}
