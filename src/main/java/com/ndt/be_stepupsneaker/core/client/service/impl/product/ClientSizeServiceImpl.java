package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientSizeRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientSizeResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientSizeMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientSizeRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientSizeService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Size;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientSizeServiceImpl implements ClientSizeService {
    @Autowired
    private ClientSizeRepository clientSizeRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<ClientSizeResponse> findAllEntity(ClientSizeRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        Page<Size> resp = clientSizeRepository.findAllSize(request, request.getStatus(), pageable);
        Page<ClientSizeResponse> clientSizeResponses = resp.map(ClientSizeMapper.INSTANCE::sizeToClientSizeResponse);
        return new PageableObject<>(clientSizeResponses);
    }
}
