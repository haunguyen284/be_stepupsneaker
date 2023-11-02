package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderHistoryMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminOrderHistoryServiceImpl implements AdminOrderHistoryService {

    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final PaginationUtil paginationUtil;

    public AdminOrderHistoryServiceImpl(
            AdminOrderRepository adminOrderRepository,
            AdminOrderHistoryRepository adminOrderHistoryRepository,
            PaginationUtil paginationUtil
    ) {
        this.adminOrderRepository = adminOrderRepository;
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
    public AdminOrderHistoryResponse create(AdminOrderHistoryRequest orderHistoryRequest) {
        return null;
    }

    @Override
    public AdminOrderHistoryResponse update(AdminOrderHistoryRequest orderHistoryRequest) {
        return null;
    }

    @Override
    public AdminOrderHistoryResponse findById(UUID id) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }
}
