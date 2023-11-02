package com.ndt.be_stepupsneaker.core.admin.service.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentMethodRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentMethodResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;

public interface AdminPaymentMethodService extends BaseService<AdminPaymentMethodResponse, UUID, AdminPaymentMethodRequest> {

}
