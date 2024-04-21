package com.ndt.be_stepupsneaker.core.admin.service.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.UUID;

public interface AdminPromotionService extends BaseService<AdminPromotionResponse,String,AdminPromotionRequest> {
    PageableObject<AdminPromotionResponse> findAllPromotion(AdminPromotionRequest request, String productDetail, String noProductDetail);
    AdminPromotionResponse deactivateDiscount(String id);
}
