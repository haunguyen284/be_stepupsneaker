package com.ndt.be_stepupsneaker.core.client.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.core.common.base.Statistic;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.DailyStatisticUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;
    private final ClientOrderHistoryRepository clientOrderHistoryRepository;
    private final ClientCustomerRepository clientCustomerRepository;
    private final ClientAddressRepository clientAddressRepository;
    private final AdminEmployeeRepository adminEmployeeRepository;
    private final ClientVoucherRepository clientVoucherRepository;
    private final ClientVoucherHistoryRepository clientVoucherHistoryRepository;
    private final PaginationUtil paginationUtil;
    @Autowired
    public ClientOrderServiceImpl(ClientOrderRepository clientOrderRepository,
                                  ClientOrderHistoryRepository clientOrderHistoryRepository,
                                  ClientCustomerRepository clientCustomerRepository,
                                  ClientAddressRepository clientAddressRepository,
                                  AdminEmployeeRepository adminEmployeeRepository,
                                  ClientVoucherRepository clientVoucherRepository,
                                  ClientVoucherHistoryRepository clientVoucherHistoryRepository,
                                  PaginationUtil paginationUtil) {
        this.clientOrderRepository = clientOrderRepository;
        this.clientOrderHistoryRepository = clientOrderHistoryRepository;
        this.clientCustomerRepository = clientCustomerRepository;
        this.clientAddressRepository = clientAddressRepository;
        this.adminEmployeeRepository = adminEmployeeRepository;
        this.clientVoucherRepository = clientVoucherRepository;
        this.clientVoucherHistoryRepository = clientVoucherHistoryRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<ClientOrderResponse> findAllEntity(ClientOrderRequest orderRequest) {
        Pageable pageable = paginationUtil.pageable(orderRequest);
        Page<Order> resp = clientOrderRepository.findAllOrder(orderRequest, orderRequest.getStatus(), orderRequest.getType(), pageable);
        Page<ClientOrderResponse> ClientPaymentResponses = resp.map(ClientOrderMapper.INSTANCE::orderToClientOrderResponse);
        return new PageableObject<>(ClientPaymentResponses);
    }

    @Override
    public ClientOrderResponse create(ClientOrderRequest orderRequest) {
        Integer pendingOrder = clientOrderRepository.countAllByStatus(OrderStatus.PENDING);
        if (pendingOrder > EntityProperties.LENGTH_PENDING_ORDER) {
            throw new ApiException("YOU CAN ONLY CREATE 5 PENDING ORDERS AT MAX");
        }
        orderRequest.setType(OrderType.OFFLINE);
        Order orderSave = ClientOrderMapper.INSTANCE.clientOrderRequestToOrder(orderRequest);
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
        float totalMoney = 0;

        Order orderResult = clientOrderRepository.save(orderSave);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(orderResult);
        orderHistory.setNote(orderResult.getNote());
        orderHistory.setActionDescription(OrderStatus.PENDING.action_description);
        clientOrderHistoryRepository.save(orderHistory);
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderResult);
    }

    @Override
    public ClientOrderResponse update(ClientOrderRequest orderRequest) {

        Optional<Order> orderOptional = clientOrderRepository.findById(orderRequest.getId());
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
            orderSave.setVoucher(clientVoucherRepository.findById(orderRequest.getVoucher()).orElse(null));
        } else {
            orderSave.setVoucher(null);
        }
        if (orderRequest.getCustomer() != null) {
            orderSave.setCustomer(clientCustomerRepository.findById(orderRequest.getCustomer()).orElse(null));
        } else {
            orderSave.setCustomer(null);
        }
        if (orderRequest.getEmployee() != null) {
            orderSave.setEmployee(adminEmployeeRepository.findById(orderRequest.getEmployee()).orElse(null));
        } else {
            orderSave.setEmployee(null);
        }
        if (orderRequest.getAddress() != null) {
            orderSave.setAddress(clientAddressRepository.findById(orderRequest.getAddress()).orElse(null));
        } else {
            orderSave.setAddress(null);
        }
        Order order = clientOrderRepository.save(orderSave);
        Optional<OrderHistory> existingOrderHistoryOptional = clientOrderHistoryRepository.findByOrder_IdAndActionStatus(order.getId(), order.getStatus());

        if (existingOrderHistoryOptional.isEmpty()) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setActionStatus(order.getStatus());
            orderHistory.setNote(orderRequest.getOrderHistoryNote());
            orderHistory.setActionDescription(order.getStatus().action_description);
            clientOrderHistoryRepository.save(orderHistory);
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
            clientVoucherHistoryRepository.save(voucherHistory);
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderSave);
    }

    @Override
    public ClientOrderResponse findById(String id) {
        Optional<Order> orderOptional = clientOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Order> orderOptional = clientOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        Order order = orderOptional.get();
        if (order.getStatus() == OrderStatus.PENDING) {
            clientOrderHistoryRepository.deleteAllByOrder(List.of(order.getId()));
            clientOrderRepository.delete(order);
        } else {
            order.setDeleted(true);
            clientOrderRepository.save(order);

            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setNote(order.getNote());
            orderHistory.setActionDescription("Order is deleted");
            clientOrderHistoryRepository.save(orderHistory);
        }

        return true;
    }

}
