package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientMaterialRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientMaterialResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientMaterialMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientMaterialRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientMaterialService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Material;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ClientMaterialServiceImpl implements ClientMaterialService {
    @Autowired
    private ClientMaterialRepository clientMaterialRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<ClientMaterialResponse> findAllEntity(ClientMaterialRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        Page<Material> resp = clientMaterialRepository.findAllMaterial(request, request.getStatus(), pageable);
        Page<ClientMaterialResponse> clientMaterialResponses = resp.map(ClientMaterialMapper.INSTANCE::colorToClientMaterialResponse);
        return new PageableObject<>(clientMaterialResponses);
    }

}