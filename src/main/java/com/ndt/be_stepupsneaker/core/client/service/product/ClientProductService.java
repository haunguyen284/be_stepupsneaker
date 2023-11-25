package com.ndt.be_stepupsneaker.core.client.service.product;

import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminProductMapper;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientCustomerRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Product;

import java.util.Optional;

public interface ClientProductService {
    PageableObject<ClientProductResponse> findAllEntity(ClientProductRequest request);

    ClientProductResponse findById(String id);

}
