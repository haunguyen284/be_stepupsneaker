package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminOrderRequest extends PageableRequest {

    private String id;

    private String code;

    private String customer;


//    private String employee;

    private String voucher;

    private String address;

    private String phoneNumber;

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

    private String note;

    private String orderHistoryNote;

    private OrderStatus status;

    private TransactionInfo transactionInfo;

}
