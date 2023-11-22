package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientBrandRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientBrandResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientBrandService {
    PageableObject<ClientBrandResponse> findAllEntity(ClientBrandRequest request);

}
