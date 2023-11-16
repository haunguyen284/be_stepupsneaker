package com.ndt.be_stepupsneaker.core.admin.service.impl.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartResponse;
import com.ndt.be_stepupsneaker.core.admin.service.cart.AdminCartService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminCartServiceImpl implements AdminCartService {
    @Override
    public PageableObject<AdminCartResponse> findAllEntity(AdminCartRequest request) {
        return null;
    }

    @Override
    public AdminCartResponse create(AdminCartRequest request) {
        return null;
    }

    @Override
    public AdminCartResponse update(AdminCartRequest request) {
        return null;
    }

    @Override
    public AdminCartResponse findById(UUID id) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }
}
