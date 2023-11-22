package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientMaterialRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientMaterialResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;


public interface ClientMaterialService {
    PageableObject<ClientMaterialResponse> findAllEntity(ClientMaterialRequest request);

}