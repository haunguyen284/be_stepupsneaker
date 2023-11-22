package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientTradeMarkRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientTradeMarkResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientTradeMarkService {
    PageableObject<ClientTradeMarkResponse> findAllEntity(ClientTradeMarkRequest request);

}