package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientStyleRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientStyleResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientStyleMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientStyleRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientStyleService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Style;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ClientStyleServiceImpl implements ClientStyleService {
    @Autowired
    private ClientStyleRepository clientStyleRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<ClientStyleResponse> findAllEntity(ClientStyleRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        Page<Style> resp = clientStyleRepository.findAllStyle(request, request.getStatus(), pageable);
        Page<ClientStyleResponse> clientStyleResponses = resp.map(ClientStyleMapper.INSTANCE::styleToClientStyleResponse);
        return new PageableObject<>(clientStyleResponses);
    }
}
