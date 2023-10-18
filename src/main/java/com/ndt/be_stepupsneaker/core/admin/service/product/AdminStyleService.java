package com.ndt.be_stepupsneaker.core.admin.service.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSoleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminStyleRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSoleResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminStyleResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;


public interface AdminStyleService extends BaseService<AdminStyleResponse, UUID, AdminStyleRequest> {

}