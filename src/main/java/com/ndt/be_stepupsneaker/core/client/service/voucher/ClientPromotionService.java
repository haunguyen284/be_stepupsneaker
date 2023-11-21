package com.ndt.be_stepupsneaker.core.client.service.voucher;

import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientPromotionRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientPromotionResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientPromotionService extends BaseService<ClientPromotionResponse,String,ClientPromotionRequest> {
    PageableObject<ClientPromotionResponse> findAllPromotion(ClientPromotionRequest request, String productDetail, String noProductDetail);
}
