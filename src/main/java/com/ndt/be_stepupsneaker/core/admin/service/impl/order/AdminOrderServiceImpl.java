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
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderMapper;
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
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.util.SendMailAutoEntity;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.DailyStatisticUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
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
            AdminProductDetailRepository adminProductDetailRepository, EmailService emailService, ClientOrderServiceImpl clientOrderService, ClientOrderServiceImpl clientOrderServiceImpl) {
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
            throw new ApiException("YOU CAN ONLY CREATE 5 PENDING ORDERS AT MAX");
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
                .orElseThrow(() -> new ResourceNotFoundException("Employee" + EntityProperties.NOT_FOUND));
        orderSave.setEmployee(employee);
        Order orderResult = adminOrderRepository.save(orderSave);
        createOrderHistory(orderResult, OrderStatus.PENDING);
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderResult);
    }

    @Override
    public AdminOrderResponse update(AdminOrderRequest orderRequest) {
        Order orderSave = getOrderById(orderRequest);
        if (orderSave.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && orderSave.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            throw new ApiException("Orders cannot be updated while the status is being shipped or completed !");
        }
        Address address = saveAddress(orderSave, orderRequest);
        List<OrderDetail> orderDetails = adminOrderDetailRepository.findAllByOrder(orderSave);
        revertQuantityProductDetail(orderSave);
        adminOrderDetailRepository.deleteAll(orderDetails);
        float shippingFee = clientOrderServiceImpl.calculateShippingFee(address);
        float totalCart = totalCartItem(orderRequest.getCartItems());
        if (totalCart >= EntityProperties.IS_FREE_SHIPPING) {
            orderSave.setShippingMoney(0);
        } else {
            orderSave.setShippingMoney(shippingFee);
        }
        createOrderDetails(orderSave, orderRequest);
        orderSave.setOriginMoney(totalCart);
        orderSave.setEmail(orderRequest.getEmail());
        orderSave.setType(OrderType.ONLINE);
        orderSave.setFullName(orderRequest.getFullName());
        orderSave.setPhoneNumber(orderRequest.getPhoneNumber());
        orderSave.setNote(orderRequest.getNote());
        setOrderInfo(orderSave);
        clientOrderServiceImpl.applyVoucherToOrder(orderSave, orderRequest.getVoucher(), totalCart, orderSave.getShippingMoney());
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
        sendMailAutoEntity.sendMailAutoUpdateOrderToClient(adminOrderResponse, orderRequest.getEmail());
        return adminOrderResponse;
    }

    @Override
    public AdminOrderResponse findById(String id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(orderOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Order> orderOptional = adminOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER IS NOT EXIST");
        }
        Order order = orderOptional.get();
        if (order.getStatus() == OrderStatus.PENDING) {
            adminOrderHistoryRepository.deleteAllByOrder(List.of(order.getId()));
            adminOrderRepository.delete(order);
        } else {
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
            throw new ResourceNotFoundException("Order not found");
        }
        Order orderSave = orderOptional.get();
        if (orderSave.getStatus() == OrderStatus.COMPLETED) {
            throw new ApiException("Your order is in completed status!");
        }

        if (orderSave.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && orderSave.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            throw new ApiException("Orders cannot be cancel while the status is being shipped or completed !");
        }

        orderSave.setStatus(adminOrderRequest.getStatus());
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee" + EntityProperties.NOT_FOUND));
        orderSave.setEmployee(employee);
        Order newOrder = adminOrderRepository.save(orderSave);
        createOrderHistory(newOrder, adminOrderRequest.getStatus());
        AdminOrderResponse adminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(newOrder);
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        sendMailAutoEntity.sendMailAutoUpdateOrderToClient(adminOrderResponse, adminOrderResponse.getEmail());
        return null;
    }

    public void revertQuantityProductDetail(Order order) {
        List<OrderDetail> orderDetails = adminOrderDetailRepository.findAllByOrder(order);
        List<ProductDetail> productDetails = orderDetails.stream().map(orderDetail -> {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
            return productDetail;
        }).collect(Collectors.toList());
        adminProductDetailRepository.saveAll(productDetails);
    }

    private List<AdminOrderHistoryResponse> createOrderHistory(Order order, OrderStatus orderStatus) {
        List<AdminOrderHistoryResponse> clientOrderHistoryResponses = new ArrayList<>();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setActionStatus(orderStatus);
        orderHistory.setNote(order.getNote());
        orderHistory.setActionDescription(orderStatus.action_description);
        clientOrderHistoryResponses.add(AdminOrderHistoryMapper.INSTANCE.orderHistoryToAdminOrderHistoryResponse(adminOrderHistoryRepository.save(orderHistory)));
        return clientOrderHistoryResponses;
    }

    private Order getOrderById(AdminOrderRequest orderRequest) {
        Optional<Order> orderOptional = adminOrderRepository.findById(orderRequest.getId());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("ORDER" + EntityProperties.NOT_EXIST);
        }

        return orderOptional.get();
    }

    public Address saveAddress(Order order, AdminOrderRequest adminOrderRequest) {
        Address address = adminAddressRepository.findById(order.getAddress().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Address" + EntityProperties.NOT_FOUND));
        address.setMore(adminOrderRequest.getAddressShipping().getMore());
        address.setDistrictId(adminOrderRequest.getAddressShipping().getDistrictId());
        address.setDistrictName(adminOrderRequest.getAddressShipping().getDistrictName());
        address.setProvinceId(adminOrderRequest.getAddressShipping().getProvinceId());
        address.setProvinceName(adminOrderRequest.getAddressShipping().getProvinceName());
        address.setWardCode(adminOrderRequest.getAddressShipping().getWardCode());
        address.setWardName(adminOrderRequest.getAddressShipping().getWardName());
        if (address.getCustomer() == null) {
            address.setCustomer(null);
        }
        return adminAddressRepository.save(address);
    }

    public float totalCartItem(List<AdminCartItemRequest> adminCartItemRequests) {
        List<ProductDetail> productDetails = new ArrayList<>();
        float total = 0.0f;
        if (adminCartItemRequests != null) {
            for (AdminCartItemRequest request : adminCartItemRequests) {
                ProductDetail productDetail = adminProductDetailRepository.findById(request.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("ProductDetail Not Found"));
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
                    .orElseThrow(() -> new ResourceNotFoundException("ProductDetail Not Found !"));
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
        if (order.getEmail() != null) {
            Customer customer = adminCustomerRepository.findByEmail(order.getEmail()).orElse(null);
            order.setCustomer(customer);
        }
        AdminEmployeeResponse employeeResponse = mySessionInfo.getCurrentEmployee();
        if (employeeResponse != null) {
            Employee employee = adminEmployeeRepository.findById(employeeResponse.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee" + EntityProperties.NOT_FOUND));
            order.setEmployee(employee);
        } else {
            throw new ResourceNotFoundException("Please! Login!");
        }
    }


}
