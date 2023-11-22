package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientBrandRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientBrandResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientBrandMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientBrandRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientBrandService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientBrandServiceImpl implements ClientBrandService {
    @Autowired
    private ClientBrandRepository clientBrandRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<ClientBrandResponse> findAllEntity(ClientBrandRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        Page<Brand> resp = clientBrandRepository.findAllBrand(request, request.getStatus(), pageable);
        Page<ClientBrandResponse> adminBrandResponses = resp.map(ClientBrandMapper.INSTANCE::brandToClientBrandResponse);
        return new PageableObject<>(adminBrandResponses);
    }

}
