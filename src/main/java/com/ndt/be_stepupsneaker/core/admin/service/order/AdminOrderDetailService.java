package com.ndt.be_stepupsneaker.core.admin.service.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface AdminOrderDetailService extends BaseService<AdminOrderDetailResponse, String, AdminOrderDetailRequest> {

    List<AdminOrderDetailResponse> create(List<AdminOrderDetailRequest> orderDetailRequests);
    List<AdminOrderDetailResponse> update(List<AdminOrderDetailRequest> orderDetailRequests);
    AdminOrderDetailResponse update(AdminOrderDetailRequest orderDetailRequest);

}
