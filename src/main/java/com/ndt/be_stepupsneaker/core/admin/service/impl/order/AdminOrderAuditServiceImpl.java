package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderAuditService;
import com.ndt.be_stepupsneaker.entity.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderAuditServiceImpl implements AdminOrderAuditService {

    private final AdminOrderRepository adminOrderRepository;

    @Override
    public List<AdminOrderResponse> getOrderRevisions(String id) {
        List<Order> orderList = adminOrderRepository.findRevisions(id).stream().map(Revision::getEntity).toList();
        return orderList.stream().map(AdminOrderMapper.INSTANCE::orderToAdminOrderResponse).collect(Collectors.toList());
    }

}
