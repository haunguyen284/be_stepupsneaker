package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminAddressRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final AdminCustomerRepository adminCustomerRepository;
    private final AdminAddressRepository adminAddressRepository;
    private final AdminEmployeeRepository adminEmployeeRepository;
    private final AdminVoucherRepository adminVoucherRepository;

    private final AdminVoucherHistoryRepository adminVoucherHistoryRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    public AdminOrderServiceImpl(
            AdminOrderRepository adminOrderRepository,
            AdminOrderHistoryRepository adminOrderHistoryRepository,
            AdminCustomerRepository adminCustomerRepository,
            AdminAddressRepository adminAddressRepository,
            AdminEmployeeRepository adminEmployeeRepository,
            AdminVoucherRepository adminVoucherRepository,
            AdminVoucherHistoryRepository adminVoucherHistoryRepository,
            PaginationUtil paginationUtil
    ) {
        this.adminOrderRepository = adminOrderRepository;
        this.adminOrderHistoryRepository = adminOrderHistoryRepository;
        this.adminCustomerRepository = adminCustomerRepository;
        this.adminAddressRepository = adminAddressRepository;
        this.adminEmployeeRepository = adminEmployeeRepository;
        this.adminVoucherRepository = adminVoucherRepository;
        this.adminVoucherHistoryRepository = adminVoucherHistoryRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<AdminOrderResponse> findAllEntity(AdminOrderRequest orderRequest) {
        Pageable pageable = paginationUtil.pageable(orderRequest);
        Page<Order> resp = adminOrderRepository.findAllOrder(orderRequest, orderRequest.getStatus(), pageable);
        Page<AdminOrderResponse> adminPaymentResponses = resp.map(AdminOrderMapper.INSTANCE::orderToAdminOrderResponse);
        return new PageableObject<>(adminPaymentResponses);
    }

    @Override
    public AdminOrderResponse create(AdminOrderRequest orderRequest) {
        Integer pendingOrder = adminOrderRepository.countAllByStatus(OrderStatus.PENDING);
        if (pendingOrder > EntityProperties.LENGTH_PENDING_ORDER) {
            throw new ApiException("YOU CAN ONLY CREATE 5 PENDING ORDERS AT MAX");
        }

        orderRequest.setType(OrderType.OFFLINE);
        Order orderSave = AdminOrderMapper.INSTANCE.adminOrderRequestToOrder(orderRequest);
        if (orderRequest.getCustomer() == null) {
            orderSave.setCustomer(null);
        }
        if (orderRequest.getEmployee() == null) {
            orderSave.setEmployee(null);
        }
        if (orderRequest.getVoucher() == null) {
            orderSave.setVoucher(null);
        }
        if (orderRequest.getAddress() == null) {
            orderSave.setAddress(null);
        }
        Order orderResult = adminOrderRepository.save(orderSave);

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(orderResult);
        orderHistory.setNote(orderResult.getNote());
        orderHistory.setActionDescription(OrderStatus.PENDING.action_description);
        adminOrderHistoryRepository.save(orderHistory);

        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderResult);
    }

    @Override
    public AdminOrderResponse update(AdminOrderRequest orderRequest) {

        Optional<Order> orderOptional = adminOrderRepository.findById(orderRequest.getId());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }

        Order orderSave = orderOptional.get();
        orderSave.setFullName(orderRequest.getFullName());
        orderSave.setPhoneNumber(orderRequest.getPhoneNumber());
        orderSave.setNote(orderRequest.getNote());
        orderSave.setExpectedDeliveryDate(orderRequest.getExpectedDeliveryDate());
        orderSave.setConfirmationDate(orderRequest.getConfirmationDate());
        orderSave.setReceivedDate(orderRequest.getReceivedDate());
        orderSave.setDeliveryStartDate(orderRequest.getDeliveryStartDate());
        orderSave.setStatus(orderRequest.getStatus());
        orderSave.setTotalMoney(orderRequest.getTotalMoney());
        if (orderRequest.getVoucher() != null) {
            orderSave.setVoucher(adminVoucherRepository.findById(orderRequest.getVoucher()).orElse(null));
        } else {
            orderSave.setVoucher(null);
        }
        if (orderRequest.getCustomer() != null) {
            orderSave.setCustomer(adminCustomerRepository.findById(orderRequest.getCustomer()).orElse(null));
        } else {
            orderSave.setCustomer(null);
        }
        if (orderRequest.getEmployee() != null) {
            orderSave.setEmployee(adminEmployeeRepository.findById(orderRequest.getEmployee()).orElse(null));
        } else {
            orderSave.setEmployee(null);
        }
        if (orderRequest.getAddress() != null) {
            orderSave.setAddress(adminAddressRepository.findById(orderRequest.getAddress()).orElse(null));
        } else {
            orderSave.setAddress(null);
        }

        Order order = adminOrderRepository.save(orderSave);

        Optional<OrderHistory> existingOrderHistoryOptional = adminOrderHistoryRepository.findByOrder_IdAndActionStatus(order.getId(), order.getStatus());

        if (existingOrderHistoryOptional.isEmpty()) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setActionStatus(order.getStatus());
            orderHistory.setNote(orderRequest.getOrderHistoryNote());
            orderHistory.setActionDescription(order.getStatus().action_description);
            adminOrderHistoryRepository.save(orderHistory);
        }

        // Voucher history
        if (orderRequest.getVoucher() != null) {
            float totalMoney = order.getTotalMoney();
            float voucherValue = order.getVoucher().getValue();
            float reduceMoney = order.getVoucher().getType() == VoucherType.CASH ? voucherValue : ((totalMoney * voucherValue) / 100);
            VoucherHistory voucherHistory = new VoucherHistory();
            voucherHistory.setVoucher(order.getVoucher());
            voucherHistory.setOrder(order);
            voucherHistory.setMoneyReduction(reduceMoney);
            voucherHistory.setMoneyBeforeReduction(totalMoney);
            voucherHistory.setMoneyAfterReduction(reduceMoney);
            adminVoucherHistoryRepository.save(voucherHistory);
        }
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderSave);
    }

    @Override
    public AdminOrderResponse findById(UUID id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        Order order = orderOptional.get();
        if(order.getStatus() == OrderStatus.PENDING){
            adminOrderHistoryRepository.deleteAllByOrder(List.of(order.getId()));
            adminOrderRepository.delete(order);
        }else {
            order.setDeleted(true);
            adminOrderRepository.save(order);

            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setNote(order.getNote());
            orderHistory.setActionDescription("Order is deleted");
            adminOrderHistoryRepository.save(orderHistory);
        }

        return true;
    }
}
