package com.ndt.be_stepupsneaker.core.client.service.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

public interface ClientCartService  extends BaseService<ClientCartResponse, String, ClientCartRequest> { ;
}
