package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientTradeMarkRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientStyleResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientTradeMarkResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientStyleMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientTradeMarkMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientTradeMarkRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientTradeMarkService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.Style;
import com.ndt.be_stepupsneaker.entity.product.TradeMark;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientTradeMarkServiceImpl implements ClientTradeMarkService {
    
    @Autowired
    private PaginationUtil paginationUtil;
    
    @Autowired
    private ClientTradeMarkRepository clientTradeMarkRepository;

    @Override
    public PageableObject<ClientTradeMarkResponse> findAllEntity(ClientTradeMarkRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<TradeMark> resp = clientTradeMarkRepository.findAllTradeMark(request, request.getStatus(), pageable);
        Page<ClientTradeMarkResponse> clientStyleResponses = resp.map(ClientTradeMarkMapper.INSTANCE::tradeMarkToClientTradeMarkResponse);
        return new PageableObject<>(clientStyleResponses);
    }
}