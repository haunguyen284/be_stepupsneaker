package com.ndt.be_stepupsneaker.core.admin.service.order_audit;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit.AdminOrderAuditResponse;

import java.util.List;

public interface AdminOrderAuditService {

    List<AdminOrderAuditResponse> getOrderRevisions (String id);

}
