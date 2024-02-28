package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderHistoryMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminOrderHistoryServiceImpl implements AdminOrderHistoryService {

    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    private MessageUtil messageUtil;

    public AdminOrderHistoryServiceImpl(
            AdminOrderHistoryRepository adminOrderHistoryRepository,
            PaginationUtil paginationUtil
    ) {
        this.adminOrderHistoryRepository = adminOrderHistoryRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<AdminOrderHistoryResponse> findAllEntity(AdminOrderHistoryRequest orderHistoryRequest) {
        Pageable pageable = paginationUtil.pageable(orderHistoryRequest);
        Page<OrderHistory> resp = adminOrderHistoryRepository.findAllOrderHistory(orderHistoryRequest, pageable);
        Page<AdminOrderHistoryResponse> adminOrderHistoryResponses = resp.map(AdminOrderHistoryMapper.INSTANCE::orderHistoryToAdminOrderHistoryResponse);
        return new PageableObject<>(adminOrderHistoryResponses);
    }

    @Override
    public Object create(AdminOrderHistoryRequest orderHistoryRequest) {
        return null;
    }

    @Override
    public AdminOrderHistoryResponse update(AdminOrderHistoryRequest orderHistoryRequest) {
        return null;
    }

    @Override
    public AdminOrderHistoryResponse findById(String id) {
        Optional<OrderHistory> orderHistory = adminOrderHistoryRepository.findById(id);
        if(orderHistory.isEmpty()){
            throw new RuntimeException(messageUtil.getMessage("order.order_history.notfound"));
        }
        return AdminOrderHistoryMapper.INSTANCE.orderHistoryToAdminOrderHistoryResponse(orderHistory.get());
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }
}
