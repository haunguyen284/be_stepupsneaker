package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderHistoryService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
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
    public PageableObject<AdminOrderHistoryResponse> findAllEntity(AdminOrderHistoryRequest request) {
        return null;
    }

    @Override
    public AdminOrderHistoryResponse create(AdminOrderHistoryRequest request) {
        return null;
    }

    @Override
    public AdminOrderHistoryResponse update(AdminOrderHistoryRequest request) {
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
