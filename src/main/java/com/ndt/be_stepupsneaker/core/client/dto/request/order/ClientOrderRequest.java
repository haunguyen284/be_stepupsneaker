package com.ndt.be_stepupsneaker.core.client.dto.request.order;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClientOrderRequest extends PageableRequest {

    private UUID id;

    private UUID customer;

    private UUID employee;

    private UUID voucher;

    private UUID address;

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

}
