package com.ndt.be_stepupsneaker.core.admin.dto.request.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientCartItemRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
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

    private String code;

    private String customer;


//    private String employee;

    @NotBlank(message = "Email must be not null")
    private String email;

    @NotNull(message = "AddressShipping must be not null")
    private AdminAddressRequest addressShipping;

    private List<AdminCartItemRequest> cartItems;

    private String voucher;

    private String address;

    @NotBlank(message = "PhoneNumber must be not null")
    private String phoneNumber;

    @NotBlank(message = "FullName must be not null")
    private String fullName;

    @NotNull(message = "TotalMoney must be not null")
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
