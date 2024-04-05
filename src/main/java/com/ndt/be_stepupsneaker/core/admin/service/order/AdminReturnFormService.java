package com.ndt.be_stepupsneaker.core.admin.service.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

public interface AdminReturnFormService extends BaseService<AdminReturnFormResponse, String, AdminReturnFormRequest> {
    AdminReturnFormResponse updateReturnDeliveryStatus(AdminReturnFormRequest request);


}
