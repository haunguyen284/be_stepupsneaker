package com.ndt.be_stepupsneaker.core.client.service.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientReturnFormRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientReturnFormResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientReturnFormService{
    PageableObject<ClientReturnFormResponse> findAllEntity(ClientReturnFormRequest request);
    ClientReturnFormResponse findById(String id);
    ClientReturnFormResponse findByCode(String code);

    ClientReturnFormResponse create(ClientReturnFormRequest request);
}
