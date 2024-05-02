package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminCartItemRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyGrowthResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyStatisticResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminAddressMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderHistoryMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.payment.AdminPaymentMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
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
import com.ndt.be_stepupsneaker.infrastructure.constant.*;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.content.EmailSampleContent;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final AdminAddressRepository adminAddressRepository;
    private final AdminEmployeeRepository adminEmployeeRepository;
    private final AdminVoucherRepository adminVoucherRepository;
    private final PaginationUtil paginationUtil;
    private final MySessionInfo mySessionInfo;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final EmailService emailService;
    private final MessageUtil messageUtil;
    private final AdminPaymentMethodRepository adminPaymentMethodRepository;
    private final OrderUtil orderUtil;
    private final EntityUtil entityUtil;
    private final AdminPaymentRepository adminPaymentRepository;
    private final AdminVoucherHistoryRepository adminVoucherHistoryRepository;
    private final AdminCustomerRepository adminCustomerRepository;

    @Override
    public PageableObject<AdminOrderResponse> findAllEntity(AdminOrderRequest orderRequest) {
        Employee employee = adminEmployeeRepository
                .findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));
        String employeeId = "";
        if (!employee.getRole().getName().contains(EntityProperties.ADMIN)) {
            employeeId = employee.getId();
        }
        Pageable pageable = paginationUtil.pageable(orderRequest);
        Page<Order> resp = adminOrderRepository.findAllOrder(orderRequest, orderRequest.getStatus(), orderRequest.getType(), employeeId, pageable);
        Page<AdminOrderResponse> adminPaymentResponses = resp.map(AdminOrderMapper.INSTANCE::orderToAdminOrderResponse);
        return new PageableObject<>(adminPaymentResponses);
    }

    @Override
    public Object create(AdminOrderRequest orderRequest) {
        Integer pendingOrder = adminOrderRepository.countAllByStatus(OrderStatus.PENDING);
        if (pendingOrder >= EntityProperties.LENGTH_PENDING_ORDER) {
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
            if (orderDetailOptional.isEmpty() && !cartItemRequest.getId().equals("")) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
            }
            if ((cartItemRequest.getId() == null || cartItemRequest.getId().equals(""))
                    && cartItemRequest.getProductDetailId() != null) {
                ProductDetail productDetail = adminProductDetailRepository.findById(cartItemRequest.getProductDetailId())
                        .orElseThrow(() -> new ResourceNotFoundException("product.product_detail.notfound"));
                if (cartItemRequest.getQuantity() > productDetail.getQuantity()) {
                    throw new ApiException(messageUtil.getMessage("order.not_enough_quantity"));
                }
                OrderDetail orderDetail = orderUtil.createOrderDetail(productDetail, orderSave, cartItemRequest.getQuantity(), productDetail.getPrice());
                orderDetailsUpdate.add(orderDetail);
                productDetail.setQuantity(productDetail.getQuantity() - cartItemRequest.getQuantity());
                productDetailsUpdate.add(productDetail);
                continue;
            }
            OrderDetail orderDetailUpdate = orderDetailOptional.get();
            ProductDetail productDetailUpdate = orderDetailUpdate.getProductDetail();
            if (cartItemRequest.getQuantity() > productDetailUpdate.getQuantity()) {
                throw new ApiException(messageUtil.getMessage("order.not_enough_quantity"));
            }
            int quantityChange = cartItemRequest.getQuantity() - orderDetailUpdate.getQuantity();
            float promotionValue = orderUtil.getPromotionValueOfProductDetail(productDetailUpdate);
            float newProductPrice = productDetailUpdate.getPrice() - promotionValue;
            if (orderDetailUpdate.getPrice() != newProductPrice && quantityChange > 0) {
                OrderDetail newOrderDetail = orderUtil.createOrderDetail(productDetailUpdate, orderSave, quantityChange, newProductPrice);
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
            orderUtil.revertQuantityProductDetailWhenRemoveOrderDetail(orderDetailsRemove);
            adminOrderDetailRepository.deleteAll(orderDetailsRemove);
        }

        adminProductDetailRepository.saveAll(productDetailsUpdate);
        adminOrderDetailRepository.saveAll(orderDetailsUpdate);
        float shippingFee = 0.0f;
        float totalMoney = orderUtil.totalMoneyOrderDetails(orderDetailsUpdate);
        if (orderSave.getShippingMoney() == 0) {
            shippingFee = orderSave.getShippingMoney();
        } else {
            shippingFee = orderUtil.calculateShippingFee(totalMoney, address);
        }
        orderSave.setShippingMoney(shippingFee);
        orderSave.setOriginMoney(totalMoney);
        orderSave.setEmail(orderRequest.getEmail());
        orderSave.setFullName(orderRequest.getFullName());
        orderSave.setPhoneNumber(orderRequest.getPhoneNumber());
        orderSave.setNote(orderRequest.getNote());
        orderSave.setVersionUpdate(orderSave.getVersionUpdate() + 1);
        orderSave.setEmployee(applyEmployeeToOrder());
        if (orderSave.getCustomer() == null && orderSave.getVoucher() != null) {
            orderUtil.applyVoucherToOrder(orderSave, null, totalMoney, "update");
        } else {
            orderUtil.applyVoucherToOrder(orderSave, orderRequest.getVoucher(), totalMoney, "update");
        }
        orderSave.setTotalMoney(orderSave.getTotalMoney() + orderSave.getShippingMoney());
        Order order = adminOrderRepository.save(orderSave);
        if (order.getTotalMoney() != order.getPayments().get(0).getTotalMoney()) {
            orderUtil.updatePayment(order);
        }
        AdminOrderResponse adminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(order);
        EmailSampleContent emailSampleContent = new EmailSampleContent(emailService);
        if (adminOrderResponse.getAddress() != null) {
            String subject = "Đơn hàng của bạn vừa được cập nhật!";
            emailSampleContent.sendMailAutoOrder(order, orderRequest.getEmail(), subject);
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
            adminVoucherHistoryRepository.deleteAllByOrder(order);
            adminVoucherHistoryRepository.deleteAllByOrder(order);
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
        if (orderSave.getStatus() == OrderStatus.COMPLETED
                || orderSave.getStatus() == OrderStatus.CANCELED
                || orderSave.getStatus() == OrderStatus.PENDING) {
            throw new ApiException(messageUtil.getMessage("order.can_not_update"));
        }

        orderSave.setStatus(adminOrderRequest.getStatus());
        Employee employee = adminEmployeeRepository.findById(mySessionInfo.getCurrentEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));
        orderSave.setEmployee(employee);
        Voucher voucher = orderSave.getVoucher();
        List<Payment> newPayments = new ArrayList<>();
        List<Payment> payments = orderSave.getPayments();
        if (orderSave.getStatus() == OrderStatus.CANCELED) {
            if (voucher != null) {
                voucher.setQuantity(voucher.getQuantity() + 1);
                adminVoucherRepository.save(voucher);
            }
            orderUtil.revertQuantityProductDetailWhenCancelOrder(orderSave);
            for (Payment payment : payments) {
                if (payment.getPaymentStatus() == PaymentStatus.COMPLETED && (!payment.getTransactionCode().equals("Change") || !payment.getTransactionCode().equals("Tiền thừa"))) {
                    PaymentMethod paymentMethod = adminPaymentMethodRepository.findByName("Transfer")
                            .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound")));
                    Payment cancelPayment = new Payment();
                    cancelPayment.setOrder(orderSave);
                    cancelPayment.setPaymentStatus(PaymentStatus.COMPLETED);
                    cancelPayment.setPaymentMethod(paymentMethod);
                    cancelPayment.setTransactionCode(adminOrderRequest.getOrderHistoryNote());
                    cancelPayment.setTotalMoney(-(orderSave.getTotalMoney()));
                    newPayments.add(cancelPayment);
                    break;
                }
            }
        }
        if (orderSave.getStatus() == OrderStatus.COMPLETED && orderSave.getPayments() != null) {
            for (Payment paymentCompleted : payments) {
                if (paymentCompleted.getTransactionCode().equals("PENDING")) {
                    paymentCompleted.setTransactionCode("Hoàn thành");
                }
                paymentCompleted.setPaymentStatus(PaymentStatus.COMPLETED);
                newPayments.add(paymentCompleted);
            }
        }
        adminPaymentRepository.saveAll(newPayments);
        Order newOrder = adminOrderRepository.save(orderSave);
        createOrderHistory(newOrder, adminOrderRequest.getStatus(), adminOrderRequest.getOrderHistoryNote());
        AdminOrderResponse adminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(newOrder);
        EmailSampleContent emailSampleContent = new EmailSampleContent(emailService);
        String subject = "Đơn hàng của bạn vừa thay đổi trạng thái!";
        emailSampleContent.sendMailAutoOrder(newOrder, adminOrderResponse.getEmail(), subject);
        return adminOrderResponse;
    }

    @Override
    public AdminOrderResponse checkOutAdmin(AdminOrderRequest orderRequest) {
        Order orderSave = getOrderById(orderRequest);
        List<OrderDetail> orderDetails = adminOrderDetailRepository.findAllByOrder_Id(orderSave.getId());
        float totalMoney = orderUtil.totalMoneyOrderDetails(orderDetails);
        orderSave.setShippingMoney(0);
        orderSave.setOriginMoney(totalMoney);
        orderSave.setEmail(orderRequest.getEmail());
        orderSave.setType(OrderType.OFFLINE);
        orderSave.setFullName(orderRequest.getFullName());
        orderSave.setPhoneNumber(orderRequest.getPhoneNumber());
        orderSave.setNote(orderRequest.getNote());
        orderSave.setEmployee(applyEmployeeToOrder());
        if (orderSave.getVoucher() == null) {
            orderSave.setTotalMoney(totalMoney);
        }
        if (!orderRequest.getPayments().isEmpty()) {
            orderSave.setStatus(OrderStatus.COMPLETED);
            Optional<OrderHistory> existingOrderHistoryOptional = adminOrderHistoryRepository.findByOrder_IdAndActionStatus(orderSave.getId(), orderSave.getStatus());
            if (existingOrderHistoryOptional.isEmpty()) {
                OrderHistory orderHistory = new OrderHistory();
                orderHistory.setOrder(orderSave);
                orderHistory.setActionStatus(orderSave.getStatus());
                orderHistory.setNote(orderRequest.getOrderHistoryNote());
                adminOrderHistoryRepository.save(orderHistory);
            }
            createPayment(orderSave, orderRequest);
        }
        AdminOrderResponse adminOrderResponse = AdminOrderMapper
                .INSTANCE
                .orderToAdminOrderResponse(adminOrderRepository.save(orderSave));
        return adminOrderResponse;
    }

    @Override
    public AdminOrderResponse checkoutSellDelivery(AdminOrderRequest orderRequest) {
        Order orderUpdate = getOrderById(orderRequest);
        Address address = AdminAddressMapper.INSTANCE.adminAddressRequestAddress(orderRequest.getAddressShipping());
        if (orderRequest.getAddressShipping().getCustomer() == null) {
            address.setCustomer(null);
        }
        Address addressOrder = adminAddressRepository.save(address);
        orderUpdate.setAddress(addressOrder);
        List<OrderDetail> orderDetails = adminOrderDetailRepository.findAllByOrder_Id(orderUpdate.getId());
        float totalMoney = orderUtil.totalMoneyOrderDetails(orderDetails);
        orderUpdate.setType(OrderType.ONLINE);
        orderUpdate.setOriginMoney(totalMoney);
        orderUpdate.setFullName(orderRequest.getFullName());
        orderUpdate.setPhoneNumber(orderRequest.getPhoneNumber());
        orderUpdate.setEmail(orderRequest.getEmail());
        orderUpdate.setEmployee(applyEmployeeToOrder());
        if (orderUpdate.getShippingMoney() == 0 && orderUpdate.getVoucher() == null) {
            orderUpdate.setTotalMoney(totalMoney);
        }
        orderUpdate.setExpectedDeliveryDate(orderUpdate.getCreatedAt() + EntityProperties.DELIVERY_TIME_IN_MILLIS);
        Order newOrder = adminOrderRepository.save(orderUpdate);
        newOrder.setStatus(OrderStatus.WAIT_FOR_DELIVERY);
        orderUtil.createVoucherHistory(newOrder);
        if (orderRequest.getIsCOD() == true) {
            PaymentMethod paymentMethod = adminPaymentMethodRepository.findByName("Cash")
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound")));
            Payment payment = new Payment();
            payment.setOrder(newOrder);
            payment.setPaymentMethod(paymentMethod);
            payment.setDescription(newOrder.getNote());
            payment.setTotalMoney(newOrder.getTotalMoney());
            payment.setTransactionCode(messageUtil.getMessage("payment.method.cash"));
            payment.setPaymentStatus(PaymentStatus.PENDING);
            adminPaymentRepository.save(payment);
        } else {
            createPayment(newOrder, orderRequest);
        }
        Optional<OrderHistory> existingOrderHistoryOptional = adminOrderHistoryRepository.findByOrder_IdAndActionStatus(newOrder.getId(), newOrder.getStatus());
        if (existingOrderHistoryOptional.isEmpty()) {
            orderUtil.createOrderHistory(newOrder, OrderStatus.WAIT_FOR_DELIVERY, messageUtil.getMessage("order.was.created"));
        }
        EmailSampleContent emailSampleContent = new EmailSampleContent(emailService);
        String subject = "Thông tin đơn hàng của bạn từ Step Up Sneaker";
        emailSampleContent.sendMailAutoOrder(newOrder, orderRequest.getEmail(), subject);
        AdminOrderResponse adminOrderResponse = AdminOrderMapper
                .INSTANCE
                .orderToAdminOrderResponse(adminOrderRepository.save(newOrder));
        return adminOrderResponse;
    }

    @Override
    public AdminOrderResponse applyCustomerToOrder(AdminOrderRequest orderRequest) {
        Order order = getOrderById(orderRequest);
        order.setCustomer(orderUtil.getCustomer(orderRequest));
        if (order.getCustomer() == null && order.getVoucher() != null) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            float totalMoney = orderUtil.totalMoneyOrderDetails(orderDetails);
            orderUtil.applyVoucherToOrder(order, null, totalMoney, "update");
        }
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(adminOrderRepository.save(order));
    }

    @Override
    public AdminOrderResponse applyVoucherToOrder(AdminOrderRequest orderRequest) {
        Order order = getOrderById(orderRequest);
        if (order.getOrderDetails().isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
        }
        List<OrderDetail> orderDetails = order.getOrderDetails();
        float totalMoney = orderUtil.totalMoneyOrderDetails(orderDetails);
        order.setOriginMoney(totalMoney);
        orderUtil.applyVoucherToOrder(order, orderRequest.getVoucher(), totalMoney, "update");
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(adminOrderRepository.save(order));
    }

    @Override
    public AdminOrderResponse applyShippingToOrder(AdminOrderRequest orderRequest) {
        Order order = getOrderById(orderRequest);
        float shippingFee = orderRequest.getShippingMoney();
        if (order.getOrderDetails().isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
        }
        float totalMoney = orderUtil.totalMoneyOrderDetails(order.getOrderDetails());
        order.setShippingMoney(shippingFee);
        order.setOriginMoney(totalMoney);
        if (order.getVoucher() == null) {
            order.setTotalMoney(order.getOriginMoney() + shippingFee);
        } else {
            order.setTotalMoney(order.getTotalMoney() + shippingFee);
        }
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(adminOrderRepository.save(order));
    }

    @Override
    public AdminOrderResponse applyNoteToOrder(AdminOrderRequest orderRequest) {
        Order order = getOrderById(orderRequest);
        order.setNote(orderRequest.getNote());
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(adminOrderRepository.save(order));
    }

    @Override
    public AdminOrderResponse findByCode(String code) {
        Order order = adminOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("order.notfound")));
        return AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(order);
    }

    @Override
    public void randomOrderData() {
        Random random = new Random();

        List<Order> orders = adminOrderRepository.findAll();
        List<Customer> customers = adminCustomerRepository.findAll();
        List<Address> addresses = adminAddressRepository.findAll();
        List<ProductDetail> productDetails = adminProductDetailRepository.findAll();
        List<PaymentMethod> paymentMethods = adminPaymentMethodRepository.findAll();

        for (int i = 0; i < 1000; i++) {
            Customer customer = customers.get(random.nextInt(customers.size()));
            Order order = orders.get(random.nextInt(orders.size()));
            Address address = addresses.get(random.nextInt(addresses.size()));
            ProductDetail productDetail = productDetails.get(random.nextInt(productDetails.size()));
            PaymentMethod paymentMethod = paymentMethods.get(random.nextInt(paymentMethods.size()));

            Order newOrder = new Order();
            newOrder.setCustomer(customer);
            newOrder.setEmployee(order.getEmployee());
            newOrder.setVoucher(order.getVoucher());
            newOrder.setAddress(address);
            newOrder.setPhoneNumber(order.getPhoneNumber());
            newOrder.setFullName(order.getFullName());
            newOrder.setEmail(order.getEmail());
            newOrder.setOriginMoney(order.getOriginMoney());
            newOrder.setReduceMoney(order.getReduceMoney());
            newOrder.setTotalMoney(order.getTotalMoney());
            newOrder.setShippingMoney(order.getShippingMoney());
            newOrder.setType(order.getType());
            newOrder.setNote(order.getNote());
            newOrder.setStatus(OrderStatus.COMPLETED);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long startOfYear = calendar.getTimeInMillis();
            long now = System.currentTimeMillis();

            long randomTime = startOfYear + (long) (random.nextDouble() * (now - startOfYear));
            newOrder.setCreatedAt(randomTime);
            newOrder.setUpdatedAt(randomTime);

            Order saveOrder = adminOrderRepository.save(newOrder);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductDetail(productDetail);
            orderDetail.setOrder(saveOrder);
            orderDetail.setQuantity(1);
            orderDetail.setPrice(productDetail.getPrice());
            orderDetail.setTotalPrice(productDetail.getPrice());
            orderDetail.setStatus(OrderStatus.COMPLETED);
            orderDetail.setCreatedAt(randomTime);
            orderDetail.setUpdatedAt(randomTime);

            adminOrderDetailRepository.save(orderDetail);

            Payment payment = new Payment();
            payment.setTransactionCode("Hoàn thành");
            payment.setOrder(saveOrder);
            payment.setTotalMoney(productDetail.getPrice());
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentMethod(paymentMethod);
            payment.setCreatedAt(randomTime);
            payment.setUpdatedAt(randomTime);
            adminPaymentRepository.save(payment);
        }

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
                entityUtil.updateAddress(address, orderRequest.getAddressShipping());
            }
            return adminAddressRepository.save(address);
        }
    }

    private Employee applyEmployeeToOrder() {
        AdminEmployeeResponse employeeResponse = mySessionInfo.getCurrentEmployee();
        if (employeeResponse != null) {
            return adminEmployeeRepository.findById(employeeResponse.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("employee.notfound")));
        } else {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.not_login"));
        }
    }

    private List<AdminPaymentResponse> createPayment(Order order, AdminOrderRequest orderRequest) {
        List<Payment> payments = new ArrayList<>();
        float totalPayment = 0.0f;
        for (AdminPaymentRequest paymentRequest : orderRequest.getPayments()) {
            PaymentMethod paymentMethod = adminPaymentMethodRepository.findById(paymentRequest.getPaymentMethod())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound")));
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(paymentMethod);
            payment.setTotalMoney(paymentRequest.getTotalMoney());
            if (paymentMethod.getName().equals("Cash")) {
                payment.setTransactionCode(messageUtil.getMessage("payment.method.cash"));
            } else {
                payment.setTransactionCode(paymentRequest.getTransactionCode());
            }
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setDescription(order.getNote());
            totalPayment += payment.getTotalMoney();
            payments.add(payment);
        }
        if (totalPayment > order.getTotalMoney()) {
            PaymentMethod paymentMethod = adminPaymentMethodRepository.findByName("Transfer")
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound")));
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setPaymentMethod(paymentMethod);
            payment.setTransactionCode(messageUtil.getMessage("money.change"));
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setTotalMoney(-(totalPayment - order.getTotalMoney()));
            payments.add(payment);
        }
        List<AdminPaymentResponse> adminPaymentResponses = adminPaymentRepository.saveAll(payments)
                .stream()
                .map(AdminPaymentMapper.INSTANCE::paymentToAdminPaymentResponse)
                .collect(Collectors.toList());
        return adminPaymentResponses;
    }


}
