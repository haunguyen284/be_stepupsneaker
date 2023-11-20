package com.ndt.be_stepupsneaker.core.admin.service.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionProductDetailResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface AdminPromotionProductDetailService extends BaseService<AdminPromotionProductDetailResponse, String, AdminPromotionProductDetailRequest> {
    Boolean deleteProductDetailsByPromotionId(String promotion, List<String> productDetails);
}
