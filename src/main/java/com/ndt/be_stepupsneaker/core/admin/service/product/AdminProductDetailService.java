package com.ndt.be_stepupsneaker.core.admin.service.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.List;
import java.util.UUID;

public interface AdminProductDetailService extends BaseService<AdminProductDetailResponse, String, AdminProductDetailRequest> {
    List<AdminProductDetailResponse> create(List<AdminProductDetailRequest> productDetailRequests);
    List<AdminProductDetailResponse> update(List<AdminProductDetailRequest> productDetailRequests);

    PageableObject<AdminProductDetailResponse> findByTrending(Long fromDate, Long toDate);
}
