package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSizeRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSizeResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientSizeService {
    PageableObject<ClientSizeResponse> findAllEntity(ClientSizeRequest request);
}
