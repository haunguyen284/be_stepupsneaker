package com.ndt.be_stepupsneaker.core.admin.service.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;

public interface AdminPaymentService extends BaseService<AdminPaymentResponse, UUID, AdminPaymentRequest> {

}
