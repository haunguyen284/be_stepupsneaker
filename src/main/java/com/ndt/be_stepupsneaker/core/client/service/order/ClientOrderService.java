package com.ndt.be_stepupsneaker.core.client.service.order;

import ch.qos.logback.core.net.server.Client;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.order.Order;

public interface ClientOrderService extends BaseService<ClientOrderResponse, String, ClientOrderRequest> {

    ClientOrderResponse findByIdAndCustomerId(String orderId, String customerId);

    ClientOrderResponse findByCode(String code);

    Boolean cancelOrder(String code, String orderHistoryNote);

    ClientOrderResponse createCheckoutVnPay(ClientOrderRequest orderRequest);

}
