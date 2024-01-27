package com.ndt.be_stepupsneaker.core.admin.dto.response.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit.ChangeDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.NotAudited;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderResponse {

    private String id;

    private AdminCustomerResponse customer;

    private AdminEmployeeResponse employee;

    private AdminVoucherResponse voucher;

    private String phoneNumber;

    private AdminAddressResponse address;

    private String email;

    private String fullName;

    private float totalMoney;

    private float reduceMoney;

    private float originMoney;

    private float shippingMoney;

    private Long confirmationDate;

    private Long expectedDeliveryDate;

    private Long deliveryStartDate;

    private Long receivedDate;

    private Long createdAt;

    private OrderType type;

    private String note;

    private String code;

    private OrderStatus status;

    private List<AdminOrderDetailResponse> orderDetails;

    private List<AdminOrderHistoryResponse> orderHistories;

    private List<AdminPaymentResponse> payments;

}
