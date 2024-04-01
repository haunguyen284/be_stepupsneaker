package com.ndt.be_stepupsneaker.core.admin.service.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface AdminReturnFormHistoryService {
    PageableObject<AdminReturnFormHistoryResponse> findAllEntity(AdminReturnFormRequest request);

}
