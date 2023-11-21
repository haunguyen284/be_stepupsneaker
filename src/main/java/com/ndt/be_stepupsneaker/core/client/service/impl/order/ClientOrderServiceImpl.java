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
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
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
    public ClientOrderResponse create(ClientOrderRequest clientOrderRequest) {
        Integer pendingOrder = clientOrderRepository.countAllByStatus(OrderStatus.PENDING);
        if (pendingOrder > EntityProperties.LENGTH_PENDING_ORDER) {
            throw new ApiException("YOU CAN ONLY CREATE 5 PENDING ORDERS AT MAX !");
        }
        Order orderSave = ClientOrderMapper.INSTANCE.clientOrderRequestToOrder(clientOrderRequest);
        orderSave.setType(OrderType.ONLINE);
        orderSave.setStatus(OrderStatus.WAIT_FOR_CONFIRMATION);
        setOrderDetails(orderSave, clientOrderRequest);
        float totalOrderPrice = calculateTotalPriceOrderDetailOfOrder(orderSave);
        applyVoucherToOrder(orderSave, clientOrderRequest.getVoucher(), totalOrderPrice, orderSave.getShippingMoney());
        Order orderResult = clientOrderRepository.save(orderSave);
        // Voucher history
        if (clientOrderRequest.getVoucher() != null) {
            float totalMoney = orderResult.getTotalMoney();
            float voucherValue = orderResult.getVoucher().getValue();
            float reduceMoney = orderResult.getVoucher().getType() == VoucherType.CASH ? voucherValue : ((totalMoney * voucherValue) / 100);
            VoucherHistory voucherHistory = new VoucherHistory();
            voucherHistory.setVoucher(orderResult.getVoucher());
            voucherHistory.setOrder(orderResult);
            voucherHistory.setMoneyReduction(reduceMoney);
            voucherHistory.setMoneyBeforeReduction(totalMoney);
            voucherHistory.setMoneyAfterReduction(reduceMoney);
            clientVoucherHistoryRepository.save(voucherHistory);
        }
        // orderHistory
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(orderResult);
        orderHistory.setNote(orderResult.getNote());
        orderHistory.setActionDescription(OrderStatus.WAIT_FOR_CONFIRMATION.action_description);
        clientOrderHistoryRepository.save(orderHistory);
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderResult);
    }

    @Override
    public ClientOrderResponse update(ClientOrderRequest orderRequest) {

        Optional<Order> orderOptional = clientOrderRepository.findById(orderRequest.getId());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        Order newOrder = orderOptional.get();
        if (newOrder.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && newOrder.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            throw new ApiException("Orders cannot be updated while the status is being shipped or completed !");
        }
        newOrder.setFullName(orderRequest.getFullName());
        newOrder.setPhoneNumber(orderRequest.getPhoneNumber());
        newOrder.setNote(orderRequest.getNote());
        newOrder.setExpectedDeliveryDate(orderRequest.getExpectedDeliveryDate());
        newOrder.setConfirmationDate(orderRequest.getConfirmationDate());
        newOrder.setReceivedDate(orderRequest.getReceivedDate());
        newOrder.setDeliveryStartDate(orderRequest.getDeliveryStartDate());
        newOrder.setStatus(orderRequest.getStatus());
        setOrderDetails(newOrder, orderRequest);
        float totalOrderPrice = calculateTotalPriceOrderDetailOfOrder(newOrder);
        applyVoucherToOrder(newOrder, orderRequest.getVoucher(), totalOrderPrice, newOrder.getShippingMoney());
        Order order = clientOrderRepository.save(newOrder);
        Optional<OrderHistory> existingOrderHistoryOptional = clientOrderHistoryRepository.findByOrder_IdAndActionStatus(order.getId(), order.getStatus());
        if (existingOrderHistoryOptional.isEmpty()) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setActionStatus(order.getStatus());
            orderHistory.setNote(orderRequest.getOrderHistoryNote());
            orderHistory.setActionDescription(order.getStatus().action_description);
            clientOrderHistoryRepository.save(orderHistory);
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(newOrder);
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

    public float calculateTotalPriceOrderDetailOfOrder(Order order) {
        return order.getOrderDetails().stream()
                .map(OrderDetail::getTotalPrice)
                .reduce(0.0f, Float::sum);
    }

    private void setOrderDetails(Order order, ClientOrderRequest orderRequest) {
        String customerId = orderRequest.getCustomer();
        String employeeId = orderRequest.getEmployee();
        String addressId = orderRequest.getAddress();

        order.setCustomer(customerId != null ? clientCustomerRepository.findById(customerId).orElse(null) : null);
        order.setEmployee(employeeId != null ? adminEmployeeRepository.findById(employeeId).orElse(null) : null);
        order.setAddress(addressId != null ? clientAddressRepository.findById(addressId).orElse(null) : null);
    }

    private void applyVoucherToOrder(Order order, String voucherId, float totalOrderPrice, float shippingFee) {
        if (voucherId != null) {
            Voucher voucher = clientVoucherRepository.findById(voucherId).orElse(null);
            if (voucher != null) {
                float discount = voucher.getType() == VoucherType.CASH ? voucher.getValue() : (voucher.getValue() / 100) * totalOrderPrice;
                float finalTotalPrice = Math.max(0, totalOrderPrice - discount);
                order.setTotalMoney(finalTotalPrice + shippingFee);
                order.setVoucher(voucher);
            }
        }
    }


}
