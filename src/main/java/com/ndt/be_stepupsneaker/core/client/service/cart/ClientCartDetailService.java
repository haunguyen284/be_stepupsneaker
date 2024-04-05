package com.ndt.be_stepupsneaker.core.client.service.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.List;

public interface ClientCartDetailService extends BaseService<ClientCartDetailResponse, String, ClientCartDetailRequest> {

    List<ClientCartDetailResponse> merge(List<ClientCartDetailRequest> cartDetailRequests);

    List<ClientCartDetailResponse> findAll();

    Object updateQuantity(ClientCartDetailRequest request);

    Object decreaseQuantity(ClientCartDetailRequest request);

    Object deleteFromCart(String id);

    Object deleteAllFromCart();

    Object deleteAllFromCartByOrder(String orderId);
}
