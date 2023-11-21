package com.ndt.be_stepupsneaker.core.client.service.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.List;

public interface ClientOrderDetailService extends BaseService<ClientOrderDetailResponse, String, ClientOrderDetailRequest> {

    List<ClientOrderDetailResponse> create(List<ClientOrderDetailRequest> orderDetailRequests);
    List<ClientOrderDetailResponse> update(List<ClientOrderDetailRequest> orderDetailRequests);

}
