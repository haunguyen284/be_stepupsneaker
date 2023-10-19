package com.ndt.be_stepupsneaker.core.admin.service.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminMaterialRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminMaterialResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.List;
import java.util.UUID;

public interface AdminProductDetailService extends BaseService<AdminProductDetailResponse, UUID, AdminProductDetailRequest> {

}
