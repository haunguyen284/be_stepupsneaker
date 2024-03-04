package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminCartItemRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyGrowthResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyStatisticResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderHistoryMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.payment.ClientPaymentResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.payment.ClientPaymentMapper;
import com.ndt.be_stepupsneaker.core.client.service.impl.order.ClientOrderServiceImpl;
import com.ndt.be_stepupsneaker.core.common.base.Statistic;
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
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.infrastructure.constant.PaymentStatus;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.util.SendMailAutoEntity;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.DailyStatisticUtil;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final MySessionInfo mySessionInfo;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final EmailService emailService;
    private final ClientOrderServiceImpl clientOrderServiceImpl;

    @Autowired
    private MessageUtil messageUtil;
    private final AdminPaymentMethodRepository adminPaymentMethodRepository;

    @Autowired
    public AdminOrderServiceImpl(
            AdminOrderRepository adminOrderRepository,
            AdminOrderHistoryRepository adminOrderHistoryRepository,
            AdminCustomerRepository adminCustomerRepository,
            AdminAddressRepository adminAddressRepository,
            AdminEmployeeRepository adminEmployeeRepository,
            AdminVoucherRepository adminVoucherRepository,
            AdminVoucherHistoryRepository adminVoucherHistoryRepository,
            PaginationUtil paginationUtil,
            MySessionInfo mySessionInfo, AdminOrderDetailRepository adminOrderDetailRepository,
            AdminProductDetailRepository adminProductDetailRepository, EmailService emailService, ClientOrderServiceImpl clientOrderService, ClientOrderServiceImpl clientOrderServiceImpl, AdminPaymentMethodRepository adminPaymentMethodRepository) {
        this.adminOrderRepository = adminOrderRepository;
        this.adminOrderHistoryRepository = adminOrderHistoryRepository;
        this.adminCustomerRepository = adminCustomerRepository;
        this.adminAddressRepository = adminAddressRepository;
        this.adminEmployeeRepository = adminEmployeeRepository;
        this.adminVoucherRepository = adminVoucherRepository;
        this.adminVoucherHistoryRepository = adminVoucherHistoryRepository;
        this.paginationUtil = paginationUtil;
        this.mySessionInfo = mySessionInfo;
        this.adminOrderDetailRepository = adminOrderDetailRepository;
        this.adminProductDetailRepository = adminProductDetailRepository;
        this.emailService = emailService;
        this.clientOrderServiceImpl = clientOrderServiceImpl;
        this.adminPaymentMethodRepository = adminPaymentMethodRepository;
    }

    @Override
    public PageableObject<AdminOrderResponse> findAllEntity(AdminOrderRequest orderRequest) {
        Pageable pageable = paginationUtil.pageable(orderRequest);
        Page<Order> resp = adminOrderRepository.findAllOrder(orderRequest, orderRequest.getStatus(), orderRequest.getType(), pageable);
        Page<AdminOrderResponse> adminPaymentResponses = resp.map(AdminOrderMapper.INSTANCE::orderToAdminOrderResponse);
        return new PageableObject<>(adminPaymentResponses);
    }

    @Override
    public Object create(AdminOrderRequest orderRequest) {
        Integer pendingOrder = adminOrderRepository.countAllByStatus(OrderStatus.PENDING);
        if (pendingOrder > EntityProperties.LENGTH_PENDING_ORDER) {
            throw new ApiException(messageUtil.getMessage("order.create_max"));
        }

        orderRequest.setType(OrderType.OFFLINE);
        Order orderSave = AdminOrderMapper.INSTANCE.adminOrderRequestToOrder(orderRequest);
        if (orderRequest.getCustomer() == null) {
            orderSave.setCustomer(null);
        }
        if (orderRequest.getVoucher() == null) {
            orderSave.setVoucher(null);
        }
        if (orderRequest.getAddress() == null) {
            orderSave.setAddress(null);
        }
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));
        orderSave.setEmployee(employee);
        Order orderResult = adminOrderRepository.save(orderSave);
        createOrderHistory(orderResult, OrderStatus.PENDING, orderRequest.getOrderHistoryNote());
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderResult);
    }

    @Transactional
    @Override
    public AdminOrderResponse update(AdminOrderRequest orderRequest) {
        Order orderSave = getOrderById(orderRequest);
        if (orderSave.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && orderSave.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION && orderSave.getStatus() != OrderStatus.PENDING) {
            throw new ApiException(messageUtil.getMessage("order.can_not_update"));
        }
        Address address = null;
        if (orderRequest.getAddressShipping() == null) {
            orderSave.setAddress(null);
        } else {
            address = saveAddress(orderSave, orderRequest);

        }
        List<OrderDetail> orderDetailsUpdate = new ArrayList<>();
        List<ProductDetail> productDetailsUpdate = new ArrayList<>();
        List<OrderDetail> orderDetailsRemove = new ArrayList<>();
        for (AdminCartItemRequest cartItemRequest : orderRequest.getCartItems()) {
            Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(cartItemRequest.getId());
            if (orderDetailOptional.isEmpty()) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
            }
            OrderDetail orderDetailUpdate = orderDetailOptional.get();
            ProductDetail productDetailUpdate = orderDetailUpdate.getProductDetail();
            if (cartItemRequest.getQuantity() > productDetailUpdate.getQuantity()) {
                throw new ApiException(messageUtil.getMessage("order.not_enough_quantity"));
            }
            int quantityChange = cartItemRequest.getQuantity() - orderDetailUpdate.getQuantity();
            float promotionValue = clientOrderServiceImpl.getPromotionValueOfProductDetail(productDetailUpdate);
            float newProductPrice = productDetailUpdate.getPrice() - promotionValue;
            if (orderDetailUpdate.getPrice() != newProductPrice && quantityChange > 0) {
                OrderDetail newOrderDetail = new OrderDetail();
                newOrderDetail.setProductDetail(productDetailUpdate);
                newOrderDetail.setQuantity(quantityChange);
                newOrderDetail.setPrice(newProductPrice);
                newOrderDetail.setTotalPrice(newProductPrice * quantityChange);
                newOrderDetail.setOrder(orderSave);
                orderDetailsUpdate.add(newOrderDetail);
            } else {
                orderDetailUpdate.setQuantity(cartItemRequest.getQuantity());
                orderDetailUpdate.setTotalPrice(cartItemRequest.getQuantity() * orderDetailUpdate.getPrice());
            }

            orderDetailsUpdate.add(orderDetailUpdate);
            productDetailUpdate.setQuantity(productDetailUpdate.getQuantity() - quantityChange);
            productDetailsUpdate.add(productDetailUpdate);
        }
        List<String> cartItemIds = orderRequest.getCartItems().stream()
                .map(AdminCartItemRequest::getId)
                .collect(Collectors.toList());

        for (OrderDetail orderDetail : orderSave.getOrderDetails()) {
            if (!cartItemIds.contains(orderDetail.getId())) {
                orderDetailsRemove.add(orderDetail);
                orderDetailsUpdate.remove(orderDetail);
            }
        }
        if (orderDetailsRemove.size() > 0) {
            clientOrderServiceImpl.revertQuantityProductDetailWhenRemoveOrderDetail(orderDetailsRemove);
            adminOrderDetailRepository.deleteAll(orderDetailsRemove);
        }

        adminProductDetailRepository.saveAll(productDetailsUpdate);
        adminOrderDetailRepository.saveAll(orderDetailsUpdate);

        float totalMoney = clientOrderServiceImpl.totalMoneyOrderDetails(orderDetailsUpdate);
        float shippingFee = clientOrderServiceImpl.calculateShippingFee(totalMoney, address);
        orderSave.setShippingMoney(shippingFee);
        orderSave.setOriginMoney(totalMoney);
        orderSave.setEmail(orderRequest.getEmail());
        orderSave.setType(OrderType.ONLINE);
        orderSave.setFullName(orderRequest.getFullName());
        orderSave.setPhoneNumber(orderRequest.getPhoneNumber());
        orderSave.setNote(orderRequest.getNote());
        setOrderInfo(orderSave);
        clientOrderServiceImpl.applyVoucherToOrder(orderSave, orderRequest.getVoucher(), totalMoney, orderSave.getShippingMoney());
        Order order = adminOrderRepository.save(orderSave);
//        Optional<OrderHistory> existingOrderHistoryOptional = clientOrderHistoryRepository.findByOrder_IdAndActionStatus(order.getId(), order.getStatus());
//        if (existingOrderHistoryOptional.isEmpty()) {
//            OrderHistory orderHistory = new OrderHistory();
//            orderHistory.setOrder(order);
//            orderHistory.setActionStatus(order.getStatus());
//            orderHistory.setNote(orderRequest.getOrderHistoryNote());
//            orderHistory.setActionDescription(order.getStatus().action_description);
//            clientOrderHistoryRepository.save(orderHistory);
//        }
        AdminOrderResponse adminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(order);
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        if (adminOrderResponse.getAddress() != null) {
            sendMailAutoEntity.sendMailAutoUpdateOrderToClient(adminOrderResponse, orderRequest.getEmail());
        }
        return adminOrderResponse;
    }

    @Override
    public AdminOrderResponse findById(String id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }
        Order order = orderOptional.get();
        if (order.getStatus() == OrderStatus.PENDING) {
            if (order.getOrderDetails() != null) {
                adminOrderDetailRepository.deleteAll(order.getOrderDetails());
            }

            adminOrderHistoryRepository.deleteAllByOrder(List.of(order.getId()));
            adminOrderRepository.delete(order);
        } else {
            order.setDeleted(true);
            adminOrderRepository.save(order);
        }
        return true;
    }

    @Override
    public AdminDailyStatisticResponse getDailyRevenueBetween(Long start, Long end) {
        List<Statistic> statistics = adminOrderRepository.getDailyRevenueBetween(start, end);
        return DailyStatisticUtil.getDailyStatisticResponse(statistics);
    }

    @Override
    public AdminDailyStatisticResponse getDailyOrdersBetween(Long start, Long end) {
        List<Statistic> statistics = adminOrderRepository.getDailyOrderBetween(start, end);
        return DailyStatisticUtil.getDailyStatisticResponse(statistics);
    }

    @Override
    public List<AdminDailyGrowthResponse> getRevenueGrowthBetween(Long start, Long end) {
        List<Statistic> statistics = adminOrderRepository.getDailyRevenueBetween(start, end);
        return DailyStatisticUtil.getDailyGrowth(statistics);
    }

    @Override
    public List<AdminDailyGrowthResponse> getOrderGrowthBetween(Long start, Long end) {
        List<Statistic> statistics = adminOrderRepository.getDailyOrderBetween(start, end);
        return DailyStatisticUtil.getDailyGrowth(statistics);
    }

    @Override
    public AdminOrderResponse confirmationOrder(AdminOrderRequest adminOrderRequest) {
        Optional<Order> orderOptional = adminOrderRepository.findById(adminOrderRequest.getId());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }
        Order orderSave = orderOptional.get();
        if (orderSave.getStatus() == OrderStatus.COMPLETED || orderSave.getStatus() == OrderStatus.CANCELED) {
            throw new ApiException(messageUtil.getMessage("order.can_not_update"));
        }

        orderSave.setStatus(adminOrderRequest.getStatus());
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));
        orderSave.setEmployee(employee);
        Voucher voucher = orderSave.getVoucher();
        if (orderSave.getStatus() == OrderStatus.CANCELED && voucher != null) {
            voucher.setQuantity(voucher.getQuantity() + 1);
            adminVoucherRepository.save(voucher);
        }
        Order newOrder = adminOrderRepository.save(orderSave);
        createOrderHistory(newOrder, adminOrderRequest.getStatus(), adminOrderRequest.getOrderHistoryNote());
        AdminOrderResponse adminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(newOrder);
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        sendMailAutoEntity.sendMailAutoUpdateOrderToClient(adminOrderResponse, adminOrderResponse.getEmail());
        return adminOrderResponse;
    }

    @Override
    public AdminOrderResponse checkOutAdmin(AdminOrderRequest orderRequest) {
        Order orderSave = getOrderById(orderRequest);
        if (orderSave.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && orderSave.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION && orderSave.getStatus() != OrderStatus.PENDING) {
            throw new ApiException(messageUtil.getMessage("order.can_not_update"));
        }
        List<OrderDetail> orderDetails = adminOrderDetailRepository.findAllByOrder_Id(orderSave.getId());
        float totalMoney = totalMoneyOrderDetails(orderDetails);
        orderSave.setShippingMoney(0);
        orderSave.setOriginMoney(totalMoney);
        orderSave.setEmail(orderRequest.getEmail());
        orderSave.setType(OrderType.OFFLINE);
        orderSave.setFullName(orderRequest.getFullName());
        orderSave.setPhoneNumber(orderRequest.getPhoneNumber());
        orderSave.setNote(orderRequest.getNote());
        orderSave.setStatus(OrderStatus.COMPLETED);
        setOrderInfo(orderSave);
        clientOrderServiceImpl.applyVoucherToOrder(orderSave, orderRequest.getVoucher(), totalMoney, orderSave.getShippingMoney());
        Order order = adminOrderRepository.save(orderSave);
        Optional<OrderHistory> existingOrderHistoryOptional = adminOrderHistoryRepository.findByOrder_IdAndActionStatus(order.getId(), order.getStatus());
        if (existingOrderHistoryOptional.isEmpty()) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrder(order);
            orderHistory.setActionStatus(order.getStatus());
            orderHistory.setNote(orderRequest.getOrderHistoryNote());
            adminOrderHistoryRepository.save(orderHistory);
        }
        AdminOrderResponse adminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(order);
        return adminOrderResponse;
    }

    public float totalMoneyOrderDetails(List<OrderDetail> orderDetails) {
        float total = 0.0f;
        if (orderDetails != null) {
            for (OrderDetail orderDetail : orderDetails) {
                total += orderDetail.getPrice() * orderDetail.getQuantity();
            }
        }
        return total;
    }


    private List<AdminOrderHistoryResponse> createOrderHistory(Order order, OrderStatus orderStatus, String orderHistoryNote) {
        List<AdminOrderHistoryResponse> clientOrderHistoryResponses = new ArrayList<>();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setActionStatus(orderStatus);
        orderHistory.setNote(orderHistoryNote);
        clientOrderHistoryResponses.add(AdminOrderHistoryMapper.INSTANCE.orderHistoryToAdminOrderHistoryResponse(adminOrderHistoryRepository.save(orderHistory)));
        return clientOrderHistoryResponses;
    }

    private Order getOrderById(AdminOrderRequest orderRequest) {
        Optional<Order> orderOptional = adminOrderRepository.findById(orderRequest.getId());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }

        return orderOptional.get();
    }

    public Address saveAddress(Order order, AdminOrderRequest orderRequest) {
        if (orderRequest.getAddress() == null) {
            order.setAddress(null);
            return null;
        } else {
            Address address = adminAddressRepository.findById(order.getAddress().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("address.notfound")));

            if (orderRequest.getAddressShipping() != null) {
                if (orderRequest.getAddressShipping().getProvinceName().equals("")) {
                    address.setProvinceName(address.getProvinceName());
                } else {
                    address.setProvinceName(orderRequest.getAddressShipping().getProvinceName());
                }
                if (orderRequest.getAddressShipping().getDistrictName().equals("")) {
                    address.setDistrictName(address.getDistrictName());
                } else {
                    address.setDistrictName(orderRequest.getAddressShipping().getDistrictName());
                }
                if (orderRequest.getAddressShipping().getWardName().equals("")) {
                    address.setWardName(address.getWardName());
                } else {
                    address.setWardName(orderRequest.getAddressShipping().getWardName());
                }
                address.setMore(orderRequest.getAddressShipping().getMore());
                address.setDistrictId(orderRequest.getAddressShipping().getDistrictId());
                address.setProvinceId(orderRequest.getAddressShipping().getProvinceId());
                address.setWardCode(orderRequest.getAddressShipping().getWardCode());
            }

            return adminAddressRepository.save(address);
        }
    }


    public float totalCartItem(List<AdminCartItemRequest> adminCartItemRequests) {
        List<ProductDetail> productDetails = new ArrayList<>();
        float total = 0.0f;
        if (adminCartItemRequests != null) {
            for (AdminCartItemRequest request : adminCartItemRequests) {
                ProductDetail productDetail = adminProductDetailRepository.findById(request.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));
                float price = productDetail.getPrice();
                total += price * request.getQuantity();
                productDetail.setQuantity(productDetail.getQuantity() - request.getQuantity());
                productDetails.add(productDetail);
            }
            adminProductDetailRepository.saveAll(productDetails);

        }
        return total;
    }

    private List<AdminOrderDetailResponse> createOrderDetails(Order order, AdminOrderRequest adminOrderRequest) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (AdminCartItemRequest clientCartItemRequest : adminOrderRequest.getCartItems()) {
            ProductDetail productDetail = adminProductDetailRepository.findById(clientCartItemRequest.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));
            if (productDetail != null) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductDetail(productDetail);
                orderDetail.setQuantity(clientCartItemRequest.getQuantity());
                orderDetail.setOrder(order);
                orderDetail.setPrice(productDetail.getPrice());
                orderDetail.setTotalPrice(orderDetail.getPrice() * orderDetail.getQuantity());
                orderDetails.add(orderDetail);
            }
        }
        return adminOrderDetailRepository.saveAll(orderDetails)
                .stream()
                .map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse)
                .collect(Collectors.toList());
    }

    private void setOrderInfo(Order order) {
        if (order.getCustomer() == null && order.getEmail() != null) {
            Customer customer = adminCustomerRepository.findByEmail(order.getEmail()).orElse(null);
            order.setCustomer(customer);
        }

        if (order.getEmployee() == null) {
            AdminEmployeeResponse employeeResponse = mySessionInfo.getCurrentEmployee();
            if (employeeResponse != null) {
                Employee employee = adminEmployeeRepository.findById(employeeResponse.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));
                order.setEmployee(employee);
            } else {
                throw new ResourceNotFoundException(messageUtil.getMessage("error.not_login"));
            }
        }
    }


}
