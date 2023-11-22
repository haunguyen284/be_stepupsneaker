package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientColorRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientColorResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientColorMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientColorRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientColorService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientColorServiceImpl implements ClientColorService {
    @Autowired
    private ClientColorRepository clientColorRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<ClientColorResponse> findAllEntity(ClientColorRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        Page<Color> resp = clientColorRepository.findAllColor(request, request.getStatus(), pageable);
        Page<ClientColorResponse> clientColorResponses = resp.map(ClientColorMapper.INSTANCE::colorToClientColorResponse);
        return new PageableObject<>(clientColorResponses);
    }
}
