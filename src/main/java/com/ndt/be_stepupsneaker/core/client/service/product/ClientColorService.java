package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientColorRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientColorResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientColorService {
    PageableObject<ClientColorResponse> findAllEntity(ClientColorRequest request);

}
