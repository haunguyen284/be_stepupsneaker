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
public class AdminOrderRequest extends PageableRequest {

    private String id;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String code;

    private String customer;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String email;

    private AdminAddressRequest addressShipping;

    private List<AdminCartItemRequest> cartItems;

    private String voucher;

    private String address;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String phoneNumber;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String fullName;

    private float totalMoney;

    private float priceMin;

    private float priceMax;

    private long startDate;

    private long endDate;

    private float shippingMoney;

    private Long confirmationDate;

    private Long expectedDeliveryDate;

    private Long deliveryStartDate;

    private Long receivedDate;

    private OrderType type;

    private boolean isCOD;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String note;

    private List<AdminPaymentRequest> payments;

    @JsonDeserialize(using = CustomStringDeserializer.class)
    private String orderHistoryNote;

    private OrderStatus status;

    private TransactionInfo transactionInfo;

}
