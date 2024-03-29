package com.ndt.be_stepupsneaker.core.admin.service.review;

import com.ndt.be_stepupsneaker.core.admin.dto.request.review.AdminReviewRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.review.AdminReviewResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

public interface AdminReviewService extends BaseService<AdminReviewResponse, String, AdminReviewRequest> {
}
