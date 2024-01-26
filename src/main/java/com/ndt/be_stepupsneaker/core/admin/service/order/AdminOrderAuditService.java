package com.ndt.be_stepupsneaker.core.admin.service.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;

import java.util.List;

public interface AdminOrderAuditService {

    List<AdminOrderResponse> getOrderRevisions (String id);

}
