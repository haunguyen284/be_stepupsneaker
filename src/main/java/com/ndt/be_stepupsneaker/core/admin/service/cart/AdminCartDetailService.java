package com.ndt.be_stepupsneaker.core.admin.service.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;

public interface AdminCartDetailService extends BaseService<AdminCartDetailResponse, UUID, AdminCartDetailRequest> {
    Boolean deleteCartDetails(AdminCartDetailRequest cartDetailRequest);
}
