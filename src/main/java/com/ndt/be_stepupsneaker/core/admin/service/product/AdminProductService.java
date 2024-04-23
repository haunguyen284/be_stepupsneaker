package com.ndt.be_stepupsneaker.core.admin.service.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;

public interface AdminProductService extends BaseService<AdminProductResponse, String, AdminProductRequest> {
    AdminProductResponse findByCode(String code);
}
