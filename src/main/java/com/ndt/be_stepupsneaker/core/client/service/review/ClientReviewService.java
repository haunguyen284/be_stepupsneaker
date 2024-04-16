package com.ndt.be_stepupsneaker.core.client.service.review;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.List;

public interface ClientReviewService extends BaseService<ClientReviewResponse, String, ClientReviewRequest> {
    List<ClientReviewResponse> create(List<ClientReviewRequest> reviewRequests);

}
