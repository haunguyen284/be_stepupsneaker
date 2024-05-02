package com.ndt.be_stepupsneaker.core.client.service.impl.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientCartItemRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.*;
import com.ndt.be_stepupsneaker.core.client.dto.response.payment.ClientPaymentResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.OrderWithReviewCountResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientAddressMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.payment.ClientPaymentMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentRepository;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.client.service.vnpay.VNPayService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.notification.NotificationEmployee;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.review.Review;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.*;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.content.EmailSampleContent;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.repository.notification.NotificationEmployeeRepository;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.OrderUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;
    private final ClientCustomerRepository clientCustomerRepository;
    private final ClientAddressRepository clientAddressRepository;
    private final ClientVoucherRepository clientVoucherRepository;
    private final PaginationUtil paginationUtil;
    private final ClientProductDetailRepository clientProductDetailRepository;
    private final ClientOrderDetailRepository clientOrderDetailRepository;
    private final ClientPaymentMethodRepository clientPaymentMethodRepository;
    private final ClientPaymentRepository clientPaymentRepository;
    private final VNPayService vnPayService;
    private final MySessionInfo mySessionInfo;
    private final NotificationEmployeeRepository notificationEmployeeRepository;
    private final EmailService emailService;
    private final MessageUtil messageUtil;
    private final OrderUtil orderUtil;

    @Override
    public PageableObject<ClientOrderResponse> findAllEntity(ClientOrderRequest orderRequest) {
        Pageable pageable = paginationUtil.pageable(orderRequest);
        Page<Order> resp = clientOrderRepository.findAllOrder(orderRequest, orderRequest.getStatus(), pageable);
        Page<ClientOrderResponse> clientOrderResponses = resp.map(ClientOrderMapper.INSTANCE::orderToClientOrderResponse);
        return new PageableObject<>(clientOrderResponses);
    }

    @Override
    public Object create(ClientOrderRequest clientOrderRequest) {
        Order orderSave = ClientOrderMapper.INSTANCE.clientOrderRequestToOrder(clientOrderRequest);
        for (ClientCartItemRequest clientCartItemRequest : clientOrderRequest.getCartItems()) {
            ProductDetail productDetail = clientProductDetailRepository.findById(clientCartItemRequest.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));
            if (productDetail != null) {
                if (productDetail.getQuantity() < clientCartItemRequest.getQuantity()) {
                    throw new ApiException(messageUtil.getMessage("order.product.exceed"));
                }
            }
        }
        orderSave.setType(OrderType.ONLINE);
        orderSave.setStatus(clientOrderRequest.getPaymentMethod().equals("Cash") ? OrderStatus.WAIT_FOR_CONFIRMATION : OrderStatus.PENDING);
        Address address = ClientAddressMapper.INSTANCE.clientAddressRequestToAddress(clientOrderRequest.getAddressShipping());
        if (clientOrderRequest.getAddressShipping().getCustomer() == null) {
            address.setCustomer(null);
        }
        Address newAddress = clientAddressRepository.save(address);
        orderSave.setAddress(newAddress);
        float totalMoney = totalCartItem(clientOrderRequest.getCartItems());
        float shippingFee = orderUtil.calculateShippingFee(totalMoney, newAddress);
        orderSave.setShippingMoney(shippingFee);
        orderSave.setOriginMoney(totalMoney);
        orderSave.setCustomer(setOrderInfo(clientOrderRequest));
        orderSave.setEmployee(null);
        orderUtil.applyVoucherToOrder(orderSave, clientOrderRequest.getVoucher(), totalMoney, "add");
        orderSave.setTotalMoney(orderSave.getTotalMoney() + shippingFee);
        orderSave.setExpectedDeliveryDate(newAddress.getCreatedAt() + EntityProperties.DELIVERY_TIME_IN_MILLIS);
        Order newOrder = clientOrderRepository.save(orderSave);
        List<ClientOrderDetailResponse> clientOrderDetailResponses = createOrderDetails(newOrder, clientOrderRequest);
        orderUtil.createVoucherHistory(newOrder);
        if (clientOrderRequest.getTransactionInfo() == null && clientOrderRequest.getPaymentMethod().equals("Card")) {
            orderUtil.createOrderHistory(newOrder, OrderStatus.PENDING, "PENDING");
            ClientOrderResponse clientOrderResponse = ClientOrderMapper.INSTANCE.orderToClientOrderResponse(newOrder);
            clientOrderResponse.setOrderDetails(clientOrderDetailResponses);
            float totalVnPay = totalVnPay(clientOrderRequest.getVoucher(), totalMoney, newOrder.getShippingMoney());
            String urlVnPay = vnPayService.createOrder((int) totalVnPay, newOrder.getId());
            EmailSampleContent emailSampleContent = new EmailSampleContent(emailService);
            emailSampleContent.sendMailAutoCheckoutVnPay(clientOrderResponse, clientOrderRequest.getEmail(), urlVnPay);
            notificationOrder(newOrder, NotificationEmployeeType.ORDER_PLACED);
            return urlVnPay;
        }
        orderUtil.createOrderHistory(newOrder, OrderStatus.WAIT_FOR_CONFIRMATION, messageUtil.getMessage("order.was.created"));
        ClientOrderResponse clientOrderResponse = ClientOrderMapper.INSTANCE.orderToClientOrderResponse(newOrder);
        if (newOrder.getPayments() == null) {
            List<ClientPaymentResponse> clientPaymentResponse = createPayment(newOrder, clientOrderRequest);
            clientOrderResponse.setPayments(clientPaymentResponse);
        }
        clientOrderResponse.setOrderDetails(clientOrderDetailResponses);
        EmailSampleContent emailSampleContent = new EmailSampleContent(emailService);
        emailSampleContent.sendMailAutoInfoOrderToClient(clientOrderResponse, clientOrderRequest.getEmail());
        notificationOrder(newOrder, NotificationEmployeeType.ORDER_PLACED);
        return clientOrderResponse;
    }

    @Transactional
    @Override
    public ClientOrderResponse update(ClientOrderRequest orderRequest) {
        Optional<Order> orderOptional = clientOrderRepository.findById(orderRequest.getId());
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }
        Order orderUpdate = orderOptional.get();
        Address address = saveAddress(orderUpdate, orderRequest);
        if (orderUpdate.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && orderUpdate.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            throw new ApiException(messageUtil.getMessage("order.can_not_update"));
        }
        List<OrderDetail> orderDetailsUpdate = new ArrayList<>();
        List<ProductDetail> productDetailsUpdate = new ArrayList<>();
        List<OrderDetail> orderDetailsRemove = new ArrayList<>();
        int countQuantityCart = 0;
        for (ClientCartItemRequest cartItemRequest : orderRequest.getCartItems()) {
            countQuantityCart += cartItemRequest.getQuantity();
            Optional<OrderDetail> orderDetailOptional = clientOrderDetailRepository.findById(cartItemRequest.getId());
            if (orderDetailOptional.isEmpty() && !cartItemRequest.getId().equals("")) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
            }
            if ((cartItemRequest.getId() == null || cartItemRequest.getId().equals(""))
                    && cartItemRequest.getProductDetailId() != null) {
                ProductDetail productDetail = clientProductDetailRepository.findById(cartItemRequest.getProductDetailId())
                        .orElseThrow(() -> new ResourceNotFoundException("product.product_detail.notfound"));
                if (cartItemRequest.getQuantity() > productDetail.getQuantity()) {
                    throw new ApiException(messageUtil.getMessage("order.not_enough_quantity"));
                }
                OrderDetail orderDetail = orderUtil.createOrderDetail(productDetail, orderUpdate, cartItemRequest.getQuantity(), productDetail.getPrice());
                orderDetailsUpdate.add(orderDetail);
                productDetail.setQuantity(productDetail.getQuantity() - cartItemRequest.getQuantity());
                productDetailsUpdate.add(productDetail);
                continue;
            }

            OrderDetail orderDetailUpdate = orderDetailOptional.get();
            ProductDetail productDetailUpdate = orderDetailUpdate.getProductDetail();
            float quantityProductDetail = productDetailUpdate.getQuantity();
            if (productDetailUpdate != null) {
                if (quantityProductDetail < cartItemRequest.getQuantity()
                        && cartItemRequest.getQuantity() != orderDetailUpdate.getQuantity()) {
                    throw new ApiException(messageUtil.getMessage("order.product.exceed"));
                }
            }
            int quantityChange = cartItemRequest.getQuantity() - orderDetailUpdate.getQuantity();
            float promotionValue = orderUtil.getPromotionValueOfProductDetail(productDetailUpdate);
            float newProductPrice = productDetailUpdate.getPrice() - promotionValue;
            // Nếu số lượng giỏ tăng và giá SP đã bị thay đổi -> tạo orderdetail mới
            if (orderDetailUpdate.getPrice() != (newProductPrice) && quantityChange > 0) {
                OrderDetail newOrderDetail = orderUtil.createOrderDetail(productDetailUpdate, orderUpdate, quantityChange, newProductPrice);
                orderDetailsUpdate.add(newOrderDetail);
            } else {
                // TH còn lại -> chỉ cập nhật số lượng giỏ
                orderDetailUpdate.setQuantity(cartItemRequest.getQuantity());
                orderDetailUpdate.setTotalPrice(cartItemRequest.getQuantity() * orderDetailUpdate.getPrice());
            }
            orderDetailsUpdate.add(orderDetailUpdate);

            // trừ / cộng số lượng của SP sau khi mua
            productDetailUpdate.setQuantity(productDetailUpdate.getQuantity() - quantityChange);
            productDetailsUpdate.add(productDetailUpdate);


        }

        List<String> cartItemIds = orderRequest.getCartItems().stream()
                .map(ClientCartItemRequest::getId)
                .collect(Collectors.toList());

        for (OrderDetail orderDetail : orderUpdate.getOrderDetails()) {
            if (!cartItemIds.contains(orderDetail.getId())) {
                orderDetailsRemove.add(orderDetail);
                orderDetailsUpdate.remove(orderDetail);
            }
        }
        if (orderDetailsRemove.size() > 0) {
            orderUtil.revertQuantityProductDetailWhenRemoveOrderDetail(orderDetailsRemove);
            clientOrderDetailRepository.deleteAll(orderDetailsRemove);
        }
        if (countQuantityCart > 5) {
            throw new ApiException(messageUtil.getMessage("order.cart_item.quantity.max"));
        }
        clientProductDetailRepository.saveAll(productDetailsUpdate);
        clientOrderDetailRepository.saveAll(orderDetailsUpdate);
        float totalMoney = orderUtil.totalMoneyOrderDetails(orderDetailsUpdate);
        float shippingFee = orderUtil.calculateShippingFee(totalMoney, address);
        orderUpdate.setShippingMoney(shippingFee);
        orderUpdate.setOriginMoney(totalMoney);
        orderUpdate.setEmail(orderRequest.getEmail());
        orderUpdate.setType(OrderType.ONLINE);
        orderUpdate.setVersionUpdate(orderUpdate.getVersionUpdate() + 1);
        orderUpdate.setFullName(orderRequest.getFullName());
        orderUpdate.setPhoneNumber(orderRequest.getPhoneNumber());
        orderUpdate.setNote(orderRequest.getNote());
        orderUpdate.setCustomer(setOrderInfo(orderRequest));
        if (orderUpdate.getCustomer() == null && orderUpdate.getVoucher() != null) {
            orderUtil.applyVoucherToOrder(orderUpdate, null, totalMoney, "update");
        } else {
            orderUtil.applyVoucherToOrder(orderUpdate, orderRequest.getVoucher(), totalMoney, "update");
        }
        orderUpdate.setTotalMoney(orderUpdate.getTotalMoney() + shippingFee);
        Order order = clientOrderRepository.save(orderUpdate);
        if (order.getTotalMoney() != order.getPayments().get(0).getTotalMoney()) {
            orderUtil.updatePayment(order);
        }
        ClientOrderResponse clientOrderResponse = ClientOrderMapper.INSTANCE.orderToClientOrderResponse(order);
        EmailSampleContent emailSampleContent = new EmailSampleContent(emailService);
        String subject = "Đơn hàng của bạn vừa được cập nhật!";
        emailSampleContent.sendMailAutoOrder(order, orderRequest.getEmail(), subject);
        notificationOrder(order, NotificationEmployeeType.ORDER_CHANGED);
        return clientOrderResponse;
    }


    @Override
    public ClientOrderResponse findById(String id) {
        Optional<Order> orderOptional = clientOrderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }


    // Tính tổng tiền CartItem gửi lên
    public float totalCartItem(List<ClientCartItemRequest> clientCartItemRequests) {
        List<ProductDetail> productDetails = new ArrayList<>();
        float total = 0.0f;

        if (clientCartItemRequests != null) {
            for (ClientCartItemRequest request : clientCartItemRequests) {
                ProductDetail productDetail = clientProductDetailRepository.findById(request.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));

                float promotionValue = orderUtil.getPromotionValueOfProductDetail(productDetail);
                float price = productDetail.getPrice() - promotionValue;
                total += price * request.getQuantity();
                productDetail.setQuantity(productDetail.getQuantity() - request.getQuantity());
                productDetails.add(productDetail);
            }
            clientProductDetailRepository.saveAll(productDetails);
        }

        return total;
    }


    // Thông tin người dùng và nhân viên set vào order
    private Customer setOrderInfo(ClientOrderRequest orderRequest) {
        if (orderRequest.getCustomer() == null) {
            Customer customer = clientCustomerRepository.findByEmailAndDeleted(orderRequest.getEmail(), false).orElse(null);
            return customer;
        }
        ClientCustomerResponse clientCustomerResponse = mySessionInfo.getCurrentCustomer();
        if (clientCustomerResponse != null) {
            Customer customer = clientCustomerRepository.findById(clientCustomerResponse.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("customer.notfound")));
            return customer;
        }
        return null;
    }


    // Tổng tiền khi thanh toán VnPay
    private float totalVnPay(String voucherId, float totalCartItem, float shippingFee) {
        if (voucherId != null && !voucherId.isBlank()) {
            Voucher voucher = clientVoucherRepository.findById(voucherId)
                    .orElse(null);
            float discount = voucher.getType() == VoucherType.CASH ? voucher.getValue() : (voucher.getValue() / 100) * totalCartItem;
            float finalTotalPrice = Math.max(0, totalCartItem - discount);
            if (totalCartItem >= EntityProperties.IS_FREE_SHIPPING) {
                return finalTotalPrice;
            }
            return finalTotalPrice + shippingFee;
        }
        return totalCartItem + shippingFee;
    }

    // Lưu địa chỉ
    public Address saveAddress(Order order, ClientOrderRequest clientOrderRequest) {
        Address address = clientAddressRepository.findById(order.getAddress().getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("address.notfound")));
        if (!clientOrderRequest.getAddressShipping().getProvinceName().equals("")) {
            address.setProvinceName(clientOrderRequest.getAddressShipping().getProvinceName());
        }
        if (!clientOrderRequest.getAddressShipping().getDistrictName().equals("")) {
            address.setDistrictName(clientOrderRequest.getAddressShipping().getDistrictName());
        }
        if (!clientOrderRequest.getAddressShipping().getWardName().equals("")) {
            address.setWardName(clientOrderRequest.getAddressShipping().getWardName());
        }
        address.setMore(clientOrderRequest.getAddressShipping().getMore());
        address.setDistrictId(clientOrderRequest.getAddressShipping().getDistrictId());
        address.setProvinceId(clientOrderRequest.getAddressShipping().getProvinceId());
        address.setWardCode(clientOrderRequest.getAddressShipping().getWardCode());
        return clientAddressRepository.save(address);
    }


    // Tạo payment
    private List<ClientPaymentResponse> createPayment(Order order, ClientOrderRequest orderRequest) {
        List<ClientPaymentResponse> clientPaymentResponses = new ArrayList<>();
        PaymentMethod paymentMethod = clientPaymentMethodRepository.findByNameMethod(orderRequest.getPaymentMethod())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("payment.method.notfound")));
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setTotalMoney(order.getTotalMoney());
        if (orderRequest.getTransactionInfo() == null) {
            payment.setTransactionCode(messageUtil.getMessage("payment.transaction_code"));
            payment.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            payment.setTransactionCode(orderRequest.getTransactionInfo().getTransactionCode());
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
        }
        payment.setDescription(order.getNote());
        clientPaymentResponses.add(ClientPaymentMapper.INSTANCE.paymentToClientPaymentResponse(clientPaymentRepository.save(payment)));
        return clientPaymentResponses;
    }


    // Tạo orderDetail
    private List<ClientOrderDetailResponse> createOrderDetails(Order order, ClientOrderRequest clientOrderRequest) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ClientCartItemRequest clientCartItemRequest : clientOrderRequest.getCartItems()) {
            ProductDetail productDetail = clientProductDetailRepository.findById(clientCartItemRequest.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));
            if (productDetail != null) {
                float promotionValue = orderUtil.getPromotionValueOfProductDetail(productDetail);
                float price = productDetail.getPrice() - promotionValue;
                OrderDetail orderDetail = orderUtil.createOrderDetail(productDetail, order, clientCartItemRequest.getQuantity(), price);
                orderDetails.add(orderDetail);
            }
        }
        return clientOrderDetailRepository.saveAll(orderDetails)
                .stream()
                .map(ClientOrderDetailMapper.INSTANCE::orderDetailToClientOrderDetailResponse)
                .collect(Collectors.toList());
    }


    @Override
    public ClientOrderResponse findByIdAndCustomerId(String orderId, String customerId) {
        if (customerId == null) {
            Optional<Order> orderOptional = clientOrderRepository.findById(orderId);
            if (orderOptional.isEmpty()) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
            }
            return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderOptional.get());
        }
        //
        Order order = clientOrderRepository.findByIdAndCustomer_Id(orderId, customerId)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("order.notfound")));
        ClientOrderResponse clientOrderResponse = ClientOrderMapper.INSTANCE.orderToClientOrderResponse(order);
        List<Review> reviews = order.getReviews();
        List<OrderDetail> orderDetails = order.getOrderDetails();
        Set<OrderDetail> responses = new HashSet<>();
        if (!reviews.isEmpty()) {
            for (OrderDetail orderDetail : orderDetails) {
                boolean isReviewed = false;
                for (Review review : reviews) {
                    if (review.getProductDetail().getId().equals(orderDetail.getProductDetail().getId())) {
                        isReviewed = true;
                        break;
                    }
                }
                if (!isReviewed) {
                    responses.add(orderDetail);
                }
            }
        } else {
            responses.addAll(orderDetails);
        }
        Set<ClientOrderDetailResponse> orderDetailResponse = responses.stream()
                .map(ClientOrderDetailMapper.INSTANCE::orderDetailToClientOrderDetailResponse)
                .collect(Collectors.toSet());
        clientOrderResponse.setOrderDetailToReview(orderDetailResponse);
        return clientOrderResponse;
    }

    @Override
    public ClientOrderResponse findByCode(String code) {
        OrderWithReviewCountResponse orderWithReviewCount = clientOrderRepository.findByCodeAndReviewCount(code)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("order.notfound")));

        Order order = orderWithReviewCount.getOrder();
        Long reviewCount = orderWithReviewCount.getCountReview();
        ClientOrderResponse clientOrderResponse = ClientOrderMapper.INSTANCE.orderToClientOrderResponse(order);
        clientOrderResponse.setCountReview(reviewCount.intValue());
        return clientOrderResponse;
    }

    @Override
    public Boolean cancelOrder(String code, String orderHistoryNote) {
        Optional<Order> orderOptional = clientOrderRepository.findByCode(code);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }
        Order order = orderOptional.get();
        if (order.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && order.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            throw new ApiException(messageUtil.getMessage("order.can_not_update"));
        }
        if (order.getVoucher() != null) {
            Voucher voucher = order.getVoucher();
            voucher.setQuantity(voucher.getQuantity() + 1);
            clientVoucherRepository.save(voucher);
        }
        orderUtil.revertQuantityProductDetailWhenCancelOrder(order);
        order.setStatus(OrderStatus.CANCELED);
        Order newOrder = clientOrderRepository.save(order);
        orderUtil.createOrderHistory(newOrder, OrderStatus.CANCELED, orderHistoryNote);
        notificationOrder(newOrder, NotificationEmployeeType.ORDER_CHANGED);
        return true;
    }

    @Override
    public ClientOrderResponse createCheckoutVnPay(ClientOrderRequest orderRequest) {
        return null;
    }


    private void notificationOrder(Order order, NotificationEmployeeType type) {
        NotificationEmployee notificationEmployee = new NotificationEmployee();
        notificationEmployee.setContent(order.getFullName() + " " + order.getCode());
        notificationEmployee.setCustomer(order.getCustomer());
        notificationEmployee.setNotificationType(type);
        notificationEmployee.setHref("orders/show/" + order.getId());
        notificationEmployee.setOrder(order);
        notificationEmployeeRepository.save(notificationEmployee);
    }
}
