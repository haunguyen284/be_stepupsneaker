package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientStyleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientStyleResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;


public interface ClientStyleService {
    PageableObject<ClientStyleResponse> findAllEntity(ClientStyleRequest request);

}