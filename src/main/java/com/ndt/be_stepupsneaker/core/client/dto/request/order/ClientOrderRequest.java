package com.ndt.be_stepupsneaker.core.client.dto.request.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.vnpay.TransactionInfo;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
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

    private String phoneNumber;

    private String fullName;

    private float shippingMoney;

    private String note;

    private String orderHistoryNote;

    private TransactionInfo transactionInfo;

    private String paymentMethod;


}
