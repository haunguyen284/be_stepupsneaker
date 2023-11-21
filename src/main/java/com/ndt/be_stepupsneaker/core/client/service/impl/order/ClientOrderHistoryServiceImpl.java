package com.ndt.be_stepupsneaker.core.client.service.impl.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderHistoryMapper;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientOrderHistoryServiceImpl implements ClientOrderHistoryService {

    private final ClientOrderHistoryRepository clientOrderHistoryRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    public ClientOrderHistoryServiceImpl(
            ClientOrderHistoryRepository clientOrderHistoryRepository,
            PaginationUtil paginationUtil
    ) {
        this.clientOrderHistoryRepository = clientOrderHistoryRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<ClientOrderHistoryResponse> findAllEntity(ClientOrderHistoryRequest orderHistoryRequest) {
        Pageable pageable = paginationUtil.pageable(orderHistoryRequest);
        Page<OrderHistory> resp = clientOrderHistoryRepository.findAllOrderHistory(orderHistoryRequest, pageable);

        Page<ClientOrderHistoryResponse> ClientOrderHistoryResponses = resp.map(ClientOrderHistoryMapper.INSTANCE::orderHistoryToClientOrderHistoryResponse);
        return new PageableObject<>(ClientOrderHistoryResponses);
    }

    @Override
    public ClientOrderHistoryResponse create(ClientOrderHistoryRequest orderHistoryRequest) {
        return null;
    }

    @Override
    public ClientOrderHistoryResponse update(ClientOrderHistoryRequest orderHistoryRequest) {
        return null;
    }

    @Override
    public ClientOrderHistoryResponse findById(String id) {
        Optional<OrderHistory> orderHistory = clientOrderHistoryRepository.findById(id);
        if (orderHistory.isEmpty()) {
            throw new RuntimeException("ORDER HISTORY IS NOT EXIST");
        }
        return ClientOrderHistoryMapper.INSTANCE.orderHistoryToClientOrderHistoryResponse(orderHistory.get());
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }
}
