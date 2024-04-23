package com.ndt.be_stepupsneaker.core.client.dto.response.order;

import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientAddressResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.payment.ClientPaymentResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherResponse;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.review.Review;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientOrderResponse {

    private String id;

    private ClientCustomerResponse customer;

    private ClientVoucherResponse voucher;

    private String phoneNumber;

    private ClientAddressResponse address;

    private String fullName;

    private String email;

    private float originMoney;

    private float reduceMoney;

    private float totalMoney;

    private float shippingMoney;

    private Long confirmationDate;

    private Long expectedDeliveryDate;

    private Long deliveryStartDate;

    private Long receivedDate;

    private Long createdAt;

    private OrderType type;

    private String note;

    private String code;

    private Integer versionUpdate;

    private OrderStatus status;

    private int countReview;

    private List<ClientOrderDetailResponse> orderDetails;

    private List<ClientOrderHistoryResponse> orderHistories;

    private List<ClientPaymentResponse> payments;

    private List<ClientReviewResponse> reviews;

    private Set<ClientOrderDetailResponse> orderDetailToReview;


}
