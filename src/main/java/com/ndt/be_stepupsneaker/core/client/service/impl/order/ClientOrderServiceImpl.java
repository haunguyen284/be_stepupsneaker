package com.ndt.be_stepupsneaker.core.client.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.repository.employee.AdminEmployeeRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientCartItemRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientShippingRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientShippingResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientAddressMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
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
    private final ClientProductDetailRepository clientProductDetailRepository;
    private final ClientOrderDetailRepository clientOrderDetailRepository;

    @Autowired
    public ClientOrderServiceImpl(ClientOrderRepository clientOrderRepository,
                                  ClientOrderHistoryRepository clientOrderHistoryRepository,
                                  ClientCustomerRepository clientCustomerRepository,
                                  ClientAddressRepository clientAddressRepository,
                                  AdminEmployeeRepository adminEmployeeRepository,
                                  ClientVoucherRepository clientVoucherRepository,
                                  ClientVoucherHistoryRepository clientVoucherHistoryRepository,
                                  PaginationUtil paginationUtil,
                                  ClientProductDetailRepository clientProductDetailRepository,
                                  ClientOrderDetailRepository clientOrderDetailRepository) {
        this.clientOrderRepository = clientOrderRepository;
        this.clientOrderHistoryRepository = clientOrderHistoryRepository;
        this.clientCustomerRepository = clientCustomerRepository;
        this.clientAddressRepository = clientAddressRepository;
        this.adminEmployeeRepository = adminEmployeeRepository;
        this.clientVoucherRepository = clientVoucherRepository;
        this.clientVoucherHistoryRepository = clientVoucherHistoryRepository;
        this.paginationUtil = paginationUtil;
        this.clientProductDetailRepository = clientProductDetailRepository;
        this.clientOrderDetailRepository = clientOrderDetailRepository;
    }

    @Override
    public PageableObject<ClientOrderResponse> findAllEntity(ClientOrderRequest orderRequest) {
        return null;
    }

    @Override
    public ClientOrderResponse create(ClientOrderRequest clientOrderRequest) {
        Order orderSave = ClientOrderMapper.INSTANCE.clientOrderRequestToOrder(clientOrderRequest);
        orderSave.setType(OrderType.ONLINE);
        orderSave.setStatus(OrderStatus.WAIT_FOR_CONFIRMATION);
        Address address = ClientAddressMapper.INSTANCE.clientAddressRequestToAddress(clientOrderRequest.getAddressShipping());
        if (clientOrderRequest.getAddressShipping().getCustomer() == null) {
            address.setCustomer(null);
        }
        Address newAddress = clientAddressRepository.save(address);
        orderSave.setAddress(newAddress);
        setOrderDetails(orderSave, clientOrderRequest);
        Order orderResult = clientOrderRepository.save(orderSave);
        // shipping
//        float shippingFee = calculateShippingFee(clientOrderRequest.getAddressShipping());
//        orderResult.setShippingMoney(shippingFee);
        // create orderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ClientCartItemRequest clientCartItemRequest : clientOrderRequest.getCartItems()) {
            ProductDetail productDetail = clientProductDetailRepository.findById(clientCartItemRequest.getId()).orElse(null);
            if (productDetail != null) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductDetail(productDetail);
                orderDetail.setQuantity(clientCartItemRequest.getQuantity());
                orderDetail.setOrder(orderResult);
                orderDetail.setPrice(productDetail.getPrice());
                orderDetail.setTotalPrice(orderDetail.getPrice() * orderDetail.getQuantity());
                orderDetails.add(orderDetail);
            }
        }
        List<OrderDetail> savedOrderDetails = clientOrderDetailRepository.saveAll(orderDetails);
        float totalOrderPrice = calculateTotalPriceOrderDetailOfOrder(savedOrderDetails);
        applyVoucherToOrder(orderSave, clientOrderRequest.getVoucher(), totalOrderPrice, orderResult.getShippingMoney());
        clientOrderRepository.save(orderResult);

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
//
//        Optional<Order> orderOptional = clientOrderRepository.findById(orderRequest.getId());
//        if (orderOptional.isEmpty()) {
//            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
//        }
//        Order newOrder = orderOptional.get();
//        if (newOrder.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && newOrder.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
//            throw new ApiException("Orders cannot be updated while the status is being shipped or completed !");
//        }
//        newOrder.setFullName(orderRequest.getFullName());
//        newOrder.setPhoneNumber(orderRequest.getPhoneNumber());
//        newOrder.setNote(orderRequest.getNote());
//        setOrderDetails(newOrder, orderRequest);
//        float totalOrderPrice = calculateTotalPriceOrderDetailOfOrder(newOrder);
//        applyVoucherToOrder(newOrder, orderRequest.getVoucher(), totalOrderPrice, newOrder.getShippingMoney());
//        Order order = clientOrderRepository.save(newOrder);
//        Optional<OrderHistory> existingOrderHistoryOptional = clientOrderHistoryRepository.findByOrder_IdAndActionStatus(order.getId(), order.getStatus());
//        if (existingOrderHistoryOptional.isEmpty()) {
//            OrderHistory orderHistory = new OrderHistory();
//            orderHistory.setOrder(order);
//            orderHistory.setActionStatus(order.getStatus());
//            orderHistory.setNote(orderRequest.getOrderHistoryNote());
//            orderHistory.setActionDescription(order.getStatus().action_description);
//            clientOrderHistoryRepository.save(orderHistory);
//        }
//        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(newOrder);
        return null;
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

    public float calculateTotalPriceOrderDetailOfOrder(List<OrderDetail> orderDetails) {
        if (orderDetails != null) {
            return orderDetails.stream()
                    .map(OrderDetail::getTotalPrice)
                    .reduce(0.0f, Float::sum);
        }
        return 0;
    }

    private void setOrderDetails(Order order, ClientOrderRequest orderRequest) {
        String customerId = orderRequest.getCustomer();
        String employeeId = orderRequest.getEmployee();
        order.setCustomer(customerId != null ? clientCustomerRepository.findById(customerId).orElse(null) : null);
        order.setEmployee(employeeId != null ? adminEmployeeRepository.findById(employeeId).orElse(null) : null);
    }

    private void applyVoucherToOrder(Order order, String voucherId, float totalOrderPrice, float shippingFee) {
        if (voucherId != null) {
            Voucher voucher = clientVoucherRepository.findById(voucherId).orElse(null);
            order.setVoucher(voucher);
            if (voucher != null) {
                float discount = voucher.getType() == VoucherType.CASH ? voucher.getValue() : (voucher.getValue() / 100) * totalOrderPrice;
                float finalTotalPrice = Math.max(0, totalOrderPrice - discount);
                order.setTotalMoney(finalTotalPrice + shippingFee);

            }
        }
    }

    private float calculateShippingFee(ClientAddressRequest addressRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", EntityProperties.VITE_GHN_USER_TOKEN);
//        headers.set("shop_id",VITE_GHN_SHOP_ID);
        ClientShippingRequest shippingRequest = createShippingRequest(addressRequest);
        HttpEntity<ClientShippingRequest> requestEntity = new HttpEntity<>(shippingRequest, headers);
        ResponseEntity<ClientShippingResponse> responseEntity = restTemplate.exchange(
                EntityProperties.GHN_API_FEE_URL,
                HttpMethod.POST,
                requestEntity,
                ClientShippingResponse.class
        );
        System.out.println("========================" + responseEntity.getBody().getFee());
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            ClientShippingResponse response = responseEntity.getBody();
            if (response != null && response.isSuccess()) {
                return response.getFee();
            }
        }
        return 0;
    }

    private ClientShippingRequest createShippingRequest(ClientAddressRequest addressRequest) {
        ClientShippingRequest shippingRequest = new ClientShippingRequest();
        shippingRequest.setFromDistrictId(1542);
        shippingRequest.setToDistrictId(Integer.parseInt(addressRequest.getDistrictId()));
        shippingRequest.setToWardCode(addressRequest.getWardCode());
        shippingRequest.setServiceId(53321);
//        shippingRequest.setShopId(Integer.parseInt(VITE_GHN_SHOP_ID));
        shippingRequest.setHeight(addressRequest.getHeight());
        shippingRequest.setLength(addressRequest.getLength());
        shippingRequest.setWeight(addressRequest.getWeight());
        shippingRequest.setWidth(addressRequest.getWidth());
        return shippingRequest;
    }


}
