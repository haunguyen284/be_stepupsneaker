package com.ndt.be_stepupsneaker.core.client.service.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartDetailResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;

public interface ClientCartDetailService extends BaseService<ClientCartDetailResponse, UUID, ClientCartDetailRequest> {
    Boolean deleteCartDetails(ClientCartDetailRequest cartDetailRequest);
}
