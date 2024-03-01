package com.ndt.be_stepupsneaker.core.client.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderMapper;
import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientCartItemRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientShippingRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.*;
import com.ndt.be_stepupsneaker.core.client.dto.response.payment.ClientPaymentResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.customer.ClientAddressMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderHistoryMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.payment.ClientPaymentMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.voucher.ClientVoucherHistoryMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientAddressRepository;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentMethodRepository;
import com.ndt.be_stepupsneaker.core.client.repository.payment.ClientPaymentRepository;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderService;
import com.ndt.be_stepupsneaker.core.client.service.vnpay.VNPayService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.cart.CartDetail;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.notification.NotificationEmployee;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import com.ndt.be_stepupsneaker.entity.product.Product;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.NotificationEmployeeType;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderType;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.email.service.EmailService;
import com.ndt.be_stepupsneaker.infrastructure.email.util.SendMailAutoEntity;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.repository.notification.NotificationEmployeeRepository;
import com.ndt.be_stepupsneaker.util.ConvertUtil;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ClientOrderRepository clientOrderRepository;
    private final ClientOrderHistoryRepository clientOrderHistoryRepository;
    private final ClientCustomerRepository clientCustomerRepository;
    private final ClientAddressRepository clientAddressRepository;
    private final ClientVoucherRepository clientVoucherRepository;
    private final ClientVoucherHistoryRepository clientVoucherHistoryRepository;
    private final PaginationUtil paginationUtil;
    private final ClientProductDetailRepository clientProductDetailRepository;
    private final ClientOrderDetailRepository clientOrderDetailRepository;
    private final ClientPaymentMethodRepository clientPaymentMethodRepository;
    private final ClientPaymentRepository clientPaymentRepository;
    private final VNPayService vnPayService;
    private final MySessionInfo mySessionInfo;
    private final NotificationEmployeeRepository notificationEmployeeRepository;
    private final EmailService emailService;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    public ClientOrderServiceImpl(ClientOrderRepository clientOrderRepository,
                                  ClientOrderHistoryRepository clientOrderHistoryRepository,
                                  ClientCustomerRepository clientCustomerRepository,
                                  ClientAddressRepository clientAddressRepository,
                                  ClientVoucherRepository clientVoucherRepository,
                                  ClientVoucherHistoryRepository clientVoucherHistoryRepository,
                                  PaginationUtil paginationUtil,
                                  ClientProductDetailRepository clientProductDetailRepository,
                                  ClientOrderDetailRepository clientOrderDetailRepository,
                                  ClientPaymentMethodRepository clientPaymentMethodRepository,
                                  ClientPaymentRepository clientPaymentRepository,
                                  VNPayService vnPayService,
                                  NotificationEmployeeRepository notificationEmployeeRepository,
                                  MySessionInfo mySessionInfo, EmailService emailService) {
        this.clientOrderRepository = clientOrderRepository;
        this.clientOrderHistoryRepository = clientOrderHistoryRepository;
        this.clientCustomerRepository = clientCustomerRepository;
        this.clientAddressRepository = clientAddressRepository;
        this.clientVoucherRepository = clientVoucherRepository;
        this.clientVoucherHistoryRepository = clientVoucherHistoryRepository;
        this.paginationUtil = paginationUtil;
        this.clientProductDetailRepository = clientProductDetailRepository;
        this.clientOrderDetailRepository = clientOrderDetailRepository;
        this.clientPaymentMethodRepository = clientPaymentMethodRepository;
        this.clientPaymentRepository = clientPaymentRepository;
        this.vnPayService = vnPayService;
        this.notificationEmployeeRepository = notificationEmployeeRepository;
        this.mySessionInfo = mySessionInfo;
        this.emailService = emailService;
    }

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
        float shippingFee = calculateShippingFee(totalMoney, newAddress);
        orderSave.setShippingMoney(shippingFee);
        orderSave.setOriginMoney(totalMoney);
        orderSave.setCustomer(setOrderInfo(clientOrderRequest));
        orderSave.setEmployee(null);
        applyVoucherToOrder(orderSave, clientOrderRequest.getVoucher(), totalMoney, orderSave.getShippingMoney());
        orderSave.setExpectedDeliveryDate(newAddress.getCreatedAt() + EntityProperties.DELIVERY_TIME_IN_MILLIS);
        Order newOrder = clientOrderRepository.save(orderSave);
        List<ClientOrderDetailResponse> clientOrderDetailResponses = createOrderDetails(newOrder, clientOrderRequest);
        List<ClientOrderHistoryResponse> clientOrderHistoryResponse = createOrderHistory(newOrder, OrderStatus.WAIT_FOR_CONFIRMATION, "Order was created");
        createVoucherHistory(newOrder);
        if (clientOrderRequest.getTransactionInfo() == null && clientOrderRequest.getPaymentMethod().equals("Card")) {
            float totalVnPay = totalVnPay(clientOrderRequest.getVoucher(), totalMoney, newOrder.getShippingMoney());
            return vnPayService.createOrder((int) totalVnPay, newOrder.getId());
        }
        ClientOrderResponse clientOrderResponse = ClientOrderMapper.INSTANCE.orderToClientOrderResponse(newOrder);
        if (newOrder.getPayments() == null) {
            List<ClientPaymentResponse> clientPaymentResponse = createPayment(newOrder, clientOrderRequest);
            clientOrderResponse.setPayments(clientPaymentResponse);
        }
        clientOrderResponse.setOrderDetails(clientOrderDetailResponses);
        clientOrderResponse.setOrderHistories(clientOrderHistoryResponse);
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        sendMailAutoEntity.sendMailAutoInfoOrderToClient(clientOrderResponse, clientOrderRequest.getEmail());
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

        for (ClientCartItemRequest cartItemRequest : orderRequest.getCartItems()) {
            Optional<OrderDetail> orderDetailOptional = clientOrderDetailRepository.findById(cartItemRequest.getId());
            if (orderDetailOptional.isEmpty()) {
                throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
            }

            OrderDetail orderDetailUpdate = orderDetailOptional.get();
            ProductDetail productDetailUpdate = orderDetailUpdate.getProductDetail();
            int quantityChange = cartItemRequest.getQuantity() - orderDetailUpdate.getQuantity();
            float promotionValue = getPromotionValueOfProductDetail(productDetailUpdate);
            float newProductPrice = productDetailUpdate.getPrice() - promotionValue;

            // Nếu số lượng giỏ tăng và giá SP đã bị thay đổi -> tạo orderdetail mới
            if (orderDetailUpdate.getPrice() != (newProductPrice) && quantityChange > 0) {
                OrderDetail newOrderDetail = new OrderDetail();
                newOrderDetail.setProductDetail(productDetailUpdate);
                newOrderDetail.setQuantity(quantityChange);
                newOrderDetail.setPrice(newProductPrice);
                newOrderDetail.setTotalPrice(newProductPrice * quantityChange);
                newOrderDetail.setOrder(orderUpdate);
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
            revertQuantityProductDetailWhenRemoveOrderDetail(orderDetailsRemove);
            clientOrderDetailRepository.deleteAll(orderDetailsRemove);
        }

        clientProductDetailRepository.saveAll(productDetailsUpdate);
        clientOrderDetailRepository.saveAll(orderDetailsUpdate);

        float totalMoney = totalMoneyOrderDetails(orderDetailsUpdate);
        float shippingFee = calculateShippingFee(totalMoney, address);

        orderUpdate.setShippingMoney(shippingFee);
        orderUpdate.setOriginMoney(totalMoney);
        orderUpdate.setEmail(orderRequest.getEmail());
        orderUpdate.setType(OrderType.ONLINE);
        orderUpdate.setFullName(orderRequest.getFullName());
        orderUpdate.setPhoneNumber(orderRequest.getPhoneNumber());
        orderUpdate.setNote(orderRequest.getNote());
        orderUpdate.setCustomer(setOrderInfo(orderRequest));
        applyVoucherToOrder(orderUpdate, orderRequest.getVoucher(), totalMoney, orderUpdate.getShippingMoney());
        Order order = clientOrderRepository.save(orderUpdate);
        ClientOrderResponse clientOrderResponse = ClientOrderMapper.INSTANCE.orderToClientOrderResponse(order);
        SendMailAutoEntity sendMailAutoEntity = new SendMailAutoEntity(emailService);
        sendMailAutoEntity.sendMailAutoInfoOrderToClient(clientOrderResponse, orderRequest.getEmail());

        // Notification update order
        notificationOrder(order, NotificationEmployeeType.ORDER_CHANGED);

        return clientOrderResponse;
    }


    // Revert lại số lượng ProductDetail khi xóa OrderDetail
    public void revertQuantityProductDetailWhenRemoveOrderDetail(List<OrderDetail> orderDetails) {
        List<ProductDetail> productDetails = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
            productDetails.add(productDetail);
        }
        clientProductDetailRepository.saveAll(productDetails);
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

    // Tính tổng tiền trong OrderDetail
    public float totalMoneyOrderDetails(List<OrderDetail> orderDetails) {
        float total = 0.0f;
        if (orderDetails != null) {
            for (OrderDetail orderDetail : orderDetails) {
                total += orderDetail.getPrice() * orderDetail.getQuantity();
            }
        }
        return total;
    }


    // Tính tổng tiền CartItem gửi lên
    public float totalCartItem(List<ClientCartItemRequest> clientCartItemRequests) {
        List<ProductDetail> productDetails = new ArrayList<>();
        float total = 0.0f;

        if (clientCartItemRequests != null) {
            for (ClientCartItemRequest request : clientCartItemRequests) {
                ProductDetail productDetail = clientProductDetailRepository.findById(request.getId())
                        .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));

                float promotionValue = getPromotionValueOfProductDetail(productDetail);
                float price = productDetail.getPrice() - promotionValue;
                total += price * request.getQuantity();
                productDetail.setQuantity(productDetail.getQuantity() - request.getQuantity());
                productDetails.add(productDetail);
            }
            clientProductDetailRepository.saveAll(productDetails);
        }

        return total;
    }

    // Kiểm tra đợt giảm giá còn hạn hoặc bắt đầu hay chưa
    public boolean isValid(PromotionProductDetail ppd) {
        Promotion promotion = ppd.getPromotion();
        if (promotion != null && promotion.getEndDate() != null && promotion.getStartDate() != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime startDate = ConvertUtil.convertLongToLocalDateTime(promotion.getStartDate());
            LocalDateTime endDate = ConvertUtil.convertLongToLocalDateTime(promotion.getEndDate());

            return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
        }
        return false;
    }


    // Tính tiền sản phẩm trong đợt giảm giá
    public Float calculateMoneyPromotion(PromotionProductDetail ppd) {
        Float promotionMoney = (ppd.getProductDetail().getPrice() * ppd.getPromotion().getValue()) / 100;
        return promotionMoney;
    }


    // Thông tin người dùng và nhân viên set vào order
    private Customer setOrderInfo(ClientOrderRequest orderRequest) {
        if (orderRequest.getCustomer() == null && orderRequest.getEmail() != null) {
            Customer customer = clientCustomerRepository.findByEmail(orderRequest.getEmail()).orElse(null);
            return customer;
        }
        return null;
    }

    // Thêm khuyến mãi vào order
    public void applyVoucherToOrder(Order order, String voucherId, float totalOrderPrice, float shippingFee) {
        if (voucherId != null && !voucherId.isBlank()) {
            Voucher voucher = clientVoucherRepository.findById(voucherId)
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound")));
            if (voucher != null) {
                if (voucher.getQuantity() < 1) {
                    throw new ResourceNotFoundException(messageUtil.getMessage("voucher.expired"));
                }
                if (voucher.getConstraint() > totalOrderPrice) {
                    throw new ResourceNotFoundException(messageUtil.getMessage("order.eligible_voucher"));
                }
                order.setVoucher(voucher);
                float discount = voucher.getType() == VoucherType.CASH ? voucher.getValue() : (voucher.getValue() / 100) * totalOrderPrice;
                float finalTotalPrice = Math.max(0, totalOrderPrice - discount);
                order.setReduceMoney(discount);
                order.setTotalMoney(finalTotalPrice + shippingFee);
                voucher.setQuantity(voucher.getQuantity() - 1);
                clientVoucherRepository.save(voucher);
            }
        } else {
            order.setReduceMoney(0);
            order.setVoucher(null);
            order.setTotalMoney(totalOrderPrice + shippingFee);
        }
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
        if (clientOrderRequest.getAddressShipping().getProvinceName().equals("")) {
            address.setProvinceName(address.getProvinceName());
        } else {
            address.setProvinceName(clientOrderRequest.getAddressShipping().getProvinceName());
        }
        if (clientOrderRequest.getAddressShipping().getDistrictName().equals("")) {
            address.setDistrictName(address.getDistrictName());
        } else {
            address.setDistrictName(clientOrderRequest.getAddressShipping().getDistrictName());
        }
        if (clientOrderRequest.getAddressShipping().getWardName().equals("")) {
            address.setWardName(address.getWardName());
        } else {
            address.setWardName(clientOrderRequest.getAddressShipping().getWardName());
        }
        address.setMore(clientOrderRequest.getAddressShipping().getMore());
        address.setDistrictId(clientOrderRequest.getAddressShipping().getDistrictId());
        address.setProvinceId(clientOrderRequest.getAddressShipping().getProvinceId());
        address.setWardCode(clientOrderRequest.getAddressShipping().getWardCode());
        return clientAddressRepository.save(address);
    }


    // Tính phí ship
    public float calculateShippingFee(float totalMoney, Address address) {
        if (totalMoney >= EntityProperties.IS_FREE_SHIPPING) { // kiểm tra free ship
            return 0.0f;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", EntityProperties.VITE_GHN_USER_TOKEN);
        headers.set("shop_id", EntityProperties.VITE_GHN_SHOP_ID);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ClientShippingRequest shippingRequest = createShippingRequest(address);
        String apiUrl = EntityProperties.GHN_API_FEE_URL;
        HttpEntity<ClientShippingRequest> requestEntity = new HttpEntity<>(shippingRequest, headers);
        ResponseEntity<ClientShippingResponse> responseEntity;
        try {
            RestTemplate restTemplate = new RestTemplate();
            responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    ClientShippingResponse.class
            );
            if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
                ClientShippingResponse response = responseEntity.getBody();
                if (response != null) {
                    ClientShippingDataResponse data = response.getData();
                    return data.getService_fee();
                } else {
                    System.out.println("API Success Response but Failed: " + response.getData());
                }
            } else {
                System.out.println("API Call Failed with Status Code: " + responseEntity.getStatusCode());
                System.out.println("API Response: " + responseEntity.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.out.println("Client Error: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            System.out.println("Server Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0.0f;
    }

    // Set giá trị cho shippingRequest để tính ship
    private ClientShippingRequest createShippingRequest(Address address) {
        ClientShippingRequest shippingRequest = new ClientShippingRequest();
        shippingRequest.setFromDistrictId(1542);
        shippingRequest.setToDistrictId(Integer.parseInt(address.getDistrictId()));
        shippingRequest.setToWardCode(address.getWardCode());
        shippingRequest.setServiceId(53321);
        shippingRequest.setHeight(15);
        shippingRequest.setLength(15);
        shippingRequest.setWeight(500);
        shippingRequest.setWidth(15);
        return shippingRequest;
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
        payment.setTransactionCode(orderRequest.getTransactionInfo() == null ? "CASH" : orderRequest.getTransactionInfo().getTransactionCode());
        payment.setDescription(order.getNote());
        clientPaymentResponses.add(ClientPaymentMapper.INSTANCE.paymentToClientPaymentResponse(clientPaymentRepository.save(payment)));
        return clientPaymentResponses;
    }

    // Tạo VoucherHistory
    private ClientVoucherHistoryResponse createVoucherHistory(Order order) {
        if (order.getVoucher() != null) {
            VoucherHistory voucherHistory = new VoucherHistory();
            float totalMoney = order.getTotalMoney();
            float voucherValue = order.getVoucher().getValue();
            float reduceMoney = order.getVoucher().getType() == VoucherType.CASH ? voucherValue : ((totalMoney * voucherValue) / 100);
            voucherHistory.setVoucher(order.getVoucher());
            voucherHistory.setOrder(order);
            voucherHistory.setMoneyReduction(reduceMoney);
            voucherHistory.setMoneyBeforeReduction(totalMoney);
            voucherHistory.setMoneyAfterReduction(reduceMoney);
            return ClientVoucherHistoryMapper
                    .INSTANCE
                    .voucherHistoryToClientVoucherHistoryResponse(clientVoucherHistoryRepository.save(voucherHistory));
        }
        return null;
    }

    // Tạo orderHistory
    private List<ClientOrderHistoryResponse> createOrderHistory(Order order, OrderStatus orderStatus, String orderHistoryNote) {
        List<ClientOrderHistoryResponse> clientOrderHistoryResponses = new ArrayList<>();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setActionStatus(orderStatus);
        orderHistory.setNote(orderHistoryNote);
        clientOrderHistoryResponses.add(ClientOrderHistoryMapper.INSTANCE.orderHistoryToClientOrderHistoryResponse(clientOrderHistoryRepository.save(orderHistory)));
        return clientOrderHistoryResponses;
    }

    // Tạo orderDetail
    private List<ClientOrderDetailResponse> createOrderDetails(Order order, ClientOrderRequest clientOrderRequest) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ClientCartItemRequest clientCartItemRequest : clientOrderRequest.getCartItems()) {
            ProductDetail productDetail = clientProductDetailRepository.findById(clientCartItemRequest.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));
            if (productDetail != null) {
                float promotionValue = getPromotionValueOfProductDetail(productDetail);
                float price = productDetail.getPrice() - promotionValue;
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductDetail(productDetail);
                orderDetail.setQuantity(clientCartItemRequest.getQuantity());
                orderDetail.setOrder(order);
                orderDetail.setPrice(price);
                orderDetail.setTotalPrice(price * orderDetail.getQuantity());
                orderDetails.add(orderDetail);
            }
        }
        return clientOrderDetailRepository.saveAll(orderDetails)
                .stream()
                .map(ClientOrderDetailMapper.INSTANCE::orderDetailToClientOrderDetailResponse)
                .collect(Collectors.toList());
    }


    // Tính giá sản phẩm thấp nhất trong promotionProductDetail theo value của Promotion
    public float getPromotionValueOfProductDetail(ProductDetail productDetail) {
        float promotionValue = 0;
        List<PromotionProductDetail> promotionProductDetailsSet = productDetail.getPromotionProductDetails();
        if (promotionProductDetailsSet != null && !promotionProductDetailsSet.isEmpty()) {
            Optional<Float> maxMoneyPromotion = promotionProductDetailsSet.stream()
                    .filter(this::isValid)
                    .map(this::calculateMoneyPromotion)
                    .max(Float::compare);
            promotionValue = maxMoneyPromotion.orElse(0.0f);
        }
        return promotionValue;
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
        Optional<Order> orderOptional = clientOrderRepository.findByIdAndCustomer_Id(orderId, customerId);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderOptional.get());
    }

    @Override
    public ClientOrderResponse findByCode(String code) {
        Order order = clientOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("order.notfound")));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.tracking.expired"));
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(order);
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
        revertQuantityProductDetail(order);
        order.setStatus(OrderStatus.CANCELED);
        Order newOrder = clientOrderRepository.save(order);
        createOrderHistory(newOrder, OrderStatus.CANCELED, orderHistoryNote);

        // Notification update order
        notificationOrder(newOrder, NotificationEmployeeType.ORDER_CHANGED);
        return true;
    }

    // revert lại số lượng khi xóa, hủy Order
    public void revertQuantityProductDetail(Order order) {
        List<OrderDetail> orderDetails = clientOrderDetailRepository.findAllByOrder(order);
        List<ProductDetail> productDetails = orderDetails.stream().map(orderDetail -> {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
            return productDetail;
        }).collect(Collectors.toList());
        clientProductDetailRepository.saveAll(productDetails);
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
