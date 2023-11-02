package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    public AdminOrderServiceImpl(
            AdminOrderRepository adminOrderRepository,
            PaginationUtil paginationUtil
    ) {
        this.adminOrderRepository = adminOrderRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<AdminOrderResponse> findAllEntity(AdminOrderRequest orderRequest) {
        Pageable pageable = paginationUtil.pageable(orderRequest);
        Page<Order> resp = adminOrderRepository.findAllOrder(orderRequest, orderRequest.getStatus(),pageable);
        Page<AdminOrderResponse> adminPaymentResponses = resp.map(AdminOrderMapper.INSTANCE::orderToAdminOrderResponse);
        return new PageableObject<>(adminPaymentResponses);
    }

    @Override
    public AdminOrderResponse create(AdminOrderRequest orderRequest) {
        Integer pendingOrder = adminOrderRepository.countAllByStatus(OrderStatus.PENDING);
        if(pendingOrder > EntityProperties.LENGTH_PENDING_ORDER){
            throw new ApiException("YOU CAN ONLY CREATE 5 PENDING ORDERS AT MAX");
        }

        Order order = adminOrderRepository.save(AdminOrderMapper.INSTANCE.adminOrderRequestToOrder(orderRequest));
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(order);
    }

    @Override
    public AdminOrderResponse update(AdminOrderRequest orderRequest) {

        Optional<Order> orderOptional = adminOrderRepository.findById(orderRequest.getId());
        if(orderOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }

        Order orderSave = orderOptional.get();
        orderSave.setFullName(orderRequest.getFullName());
        orderSave.setPhoneNumber(orderRequest.getPhoneNumber());
        orderSave.setNote(orderRequest.getNote());
        orderSave.setExpectedDeliveryDate(orderRequest.getExpectedDeliveryDate());
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderSave);
    }

    @Override
    public AdminOrderResponse findById(UUID id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if(orderOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if(orderOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        Order order = orderOptional.get();
        order.setDeleted(true);
        adminOrderRepository.save(order);
        return null;
    }
}
