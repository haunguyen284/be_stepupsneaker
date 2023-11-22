package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSoleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSoleResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;


public interface ClientSoleService {
    PageableObject<ClientSoleResponse> findAllEntity(ClientSoleRequest request);

}
