package com.ndt.be_stepupsneaker.core.client.service.impl.cart;

import com.ndt.be_stepupsneaker.core.client.dto.request.cart.ClientCartRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.cart.ClientCartResponse;
import com.ndt.be_stepupsneaker.core.client.service.cart.ClientCartService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientCartServiceImpl implements ClientCartService {
    @Override
    public PageableObject<ClientCartResponse> findAllEntity(ClientCartRequest request) {
        return null;
    }

    @Override
    public ClientCartResponse create(ClientCartRequest request) {
        return null;
    }

    @Override
    public ClientCartResponse update(ClientCartRequest request) {
        return null;
    }

    @Override
    public ClientCartResponse findById(UUID id) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }
}