package com.ndt.be_stepupsneaker.core.admin.service.cart;

import com.ndt.be_stepupsneaker.core.admin.dto.request.cart.AdminCartRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.cart.AdminCartResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.UUID;

public interface AdminCartService extends BaseService<AdminCartResponse, UUID, AdminCartRequest> {
}
