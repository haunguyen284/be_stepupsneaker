package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSoleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSoleResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientSoleMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientSoleRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientSoleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Sole;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientSoleServiceImpl implements ClientSoleService {
    @Autowired
    private ClientSoleRepository clientSoleRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<ClientSoleResponse> findAllEntity(ClientSoleRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        Page<Sole> resp = clientSoleRepository.findAllSole(request, request.getStatus(), pageable);
        Page<ClientSoleResponse> clientSoleResponses = resp.map(ClientSoleMapper.INSTANCE::colorToClientSoleResponse);
        return new PageableObject<>(clientSoleResponses);
    }
}
