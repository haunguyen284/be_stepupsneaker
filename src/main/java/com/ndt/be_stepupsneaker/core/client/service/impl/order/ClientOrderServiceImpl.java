package com.ndt.be_stepupsneaker.core.client.service.impl.order;

import com.amazonaws.services.apigateway.model.Op;
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
                    .orElseThrow(() -> new ResourceNotFoundException("ProductDetail Not Found !"));
            if (productDetail != null) {
                if (productDetail.getQuantity() < clientCartItemRequest.getQuantity()) {
                    throw new ApiException("The quantity of products you purchased exceeds the quantity in stock : " + productDetail.getProduct().getName());
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
        float shippingFee = calculateShippingFee(newAddress);
        float totalCart = totalCartItem(clientOrderRequest.getCartItems());
        if (totalCart >= EntityProperties.IS_FREE_SHIPPING) {
            orderSave.setShippingMoney(0);
        } else {
            orderSave.setShippingMoney(shippingFee);
        }
        orderSave.setOriginMoney(totalCart);
        setOrderInfo(orderSave);
        applyVoucherToOrder(orderSave, clientOrderRequest.getVoucher(), totalCart, orderSave.getShippingMoney());
        orderSave.setExpectedDeliveryDate(newAddress.getCreatedAt() + EntityProperties.DELIVERY_TIME_IN_MILLIS);
        Order newOrder = clientOrderRepository.save(orderSave);
        List<ClientOrderDetailResponse> clientOrderDetailResponses = createOrderDetails(newOrder, clientOrderRequest);
        List<ClientOrderHistoryResponse> clientOrderHistoryResponse = createOrderHistory(newOrder, OrderStatus.WAIT_FOR_CONFIRMATION);
        createVoucherHistory(newOrder);
        if (clientOrderRequest.getTransactionInfo() == null && clientOrderRequest.getPaymentMethod().equals("Card")) {
            float totalVnPay = totalVnPay(clientOrderRequest.getVoucher(), totalCart, newOrder.getShippingMoney());
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
            throw new ResourceNotFoundException("ORDER" + EntityProperties.NOT_EXIST);
        }
        Order newOrder = orderOptional.get();
        if (newOrder.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && newOrder.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            throw new ApiException("Orders cannot be updated while the status is being shipped or completed !");
        }
        Address address = saveAddress(newOrder, orderRequest);
        List<OrderDetail> orderDetails = clientOrderDetailRepository.findDistinctByOrder(newOrder);
        List<OrderDetail> newOrderDetails = new ArrayList<>();
        List<OrderDetail> removeOrderDetails = new ArrayList<>();
        List<ProductDetail> productDetails = new ArrayList<>();
        int quantityInStock = 0;
        for (OrderDetail orderDetail : orderDetails) {
            boolean found = false;
            float maxMoneyPromotionInProductDetail = maxMoneyPromotionInProductDetail(orderDetail.getProductDetail());
            for (ClientCartItemRequest cartItemRequest : orderRequest.getCartItems()) {
                if (orderDetail.getProductDetail().getId().equals(cartItemRequest.getId())
                        && orderDetail.getQuantity() != cartItemRequest.getQuantity()) {
                    if (cartItemRequest.getQuantity() > orderDetail.getProductDetail().getQuantity()) {
                        throw new ApiException("The quantity of products you purchased exceeds the quantity in stock!");
                    }
                    //
                    ProductDetail productDetail = orderDetail.getProductDetail();
                    if (maxMoneyPromotionInProductDetail == orderDetail.getPrice()) {
                        System.out.println("========ĐỢT GIẢM GIÁ GIỐNG NHAU=====");
                        if (cartItemRequest.getQuantity() > orderDetail.getQuantity()) {
                            quantityInStock = cartItemRequest.getQuantity() - orderDetail.getQuantity();
                            productDetail.setQuantity(productDetail.getQuantity() - quantityInStock);
                        } else {
                            quantityInStock = orderDetail.getQuantity() - cartItemRequest.getQuantity();
                            productDetail.setQuantity(productDetail.getQuantity() + quantityInStock);
                        }
                        productDetails.add(productDetail);
                        //
                        orderDetail.setQuantity(cartItemRequest.getQuantity());
                        orderDetail.setPrice(maxMoneyPromotionInProductDetail);
                        orderDetail.setTotalPrice(orderDetail.getPrice() * orderDetail.getQuantity());
                        newOrderDetails.add(orderDetail);
                    } else {
                        int quantity = 0;
                        quantity = cartItemRequest.getQuantity() - orderDetail.getQuantity();
                        System.out.println("========IN VÀO ĐÂY LÀ KHÁC ĐỢT GIẢM GIÁ=====");
                        OrderDetail newOrderDetail = new OrderDetail();
                        newOrderDetail.setProductDetail(productDetail);
                        newOrderDetail.setQuantity(quantity);
                        newOrderDetail.setPrice(maxMoneyPromotionInProductDetail);
                        newOrderDetail.setTotalPrice(maxMoneyPromotionInProductDetail * newOrderDetail.getQuantity());
                        newOrderDetail.setOrder(newOrder);
                        newOrderDetails.add(newOrderDetail);
                        //
                        orderDetail.setQuantity(orderDetail.getQuantity());
                        newOrderDetails.add(orderDetail);

                    }

                }
                if (orderDetail.getProductDetail().getId().equals(cartItemRequest.getId())) {
                    found = true;
                }
            }
            if (!found) {
                removeOrderDetails.add(orderDetail);
            }
        }
//        if (removeOrderDetails != null) {
//            revertQuantityProductDetailWhenRemoveOrderDetail(removeOrderDetails);
//            clientOrderDetailRepository.deleteAll(removeOrderDetails);
//        }
        if (newOrderDetails != null) {
            clientProductDetailRepository.saveAll(productDetails);
            clientOrderDetailRepository.saveAll(newOrderDetails);
        }
        float shippingFee = calculateShippingFee(address);
        List<OrderDetail> orderDetailList = clientOrderDetailRepository.getAllByOrder(newOrder);
        float totalOrderDetails = totalMoneyOrderDetails(orderDetailList);

        if (totalOrderDetails >= EntityProperties.IS_FREE_SHIPPING) {
            newOrder.setShippingMoney(0);
        } else {
            newOrder.setShippingMoney(shippingFee);
        }
        newOrder.setOriginMoney(totalOrderDetails);
        newOrder.setEmail(orderRequest.getEmail());
        newOrder.setType(OrderType.ONLINE);
        newOrder.setFullName(orderRequest.getFullName());
        newOrder.setPhoneNumber(orderRequest.getPhoneNumber());
        newOrder.setNote(orderRequest.getNote());
        setOrderInfo(newOrder);
        applyVoucherToOrder(newOrder, orderRequest.getVoucher(), totalOrderDetails, newOrder.getShippingMoney());
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
            throw new ResourceNotFoundException("ORDER" + EntityProperties.NOT_EXIST);
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
                        .orElseThrow(() -> new ResourceNotFoundException("ProductDetail Not Found"));

                float price = maxMoneyPromotionInProductDetail(productDetail);
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
    private void setOrderInfo(Order order) {
        if (order.getCustomer() == null && order.getEmail() != null) {
            Customer customer = clientCustomerRepository.findByEmail(order.getEmail()).orElse(null);
            order.setCustomer(customer);
        }
        order.setEmployee(null);
    }

    // Thêm khuyến mãi vào order
    public void applyVoucherToOrder(Order order, String voucherId, float totalOrderPrice, float shippingFee) {
        if (voucherId != null && !voucherId.isBlank()) {
            Voucher voucher = clientVoucherRepository.findById(voucherId)
                    .orElseThrow(() -> new ResourceNotFoundException("Voucher" + EntityProperties.NOT_FOUND));
            if (voucher != null) {
                if (voucher.getQuantity() < 1) {
                    throw new ResourceNotFoundException("The previous voucher has expired!");
                }
                if (voucher.getConstraint() > totalOrderPrice) {
                    throw new ResourceNotFoundException("Your order is not eligible for the voucher!");
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
                .orElseThrow(() -> new ResourceNotFoundException("Address" + EntityProperties.NOT_FOUND));
        if (clientOrderRequest.getAddressShipping().getProvinceName() != null || clientOrderRequest.getAddressShipping().getProvinceName().equals("")) {
            address.setProvinceName(clientOrderRequest.getAddressShipping().getProvinceName());
        }
        if (clientOrderRequest.getAddressShipping().getDistrictName() != null || clientOrderRequest.getAddressShipping().getDistrictName().equals("")) {
            address.setDistrictName(clientOrderRequest.getAddressShipping().getDistrictName());
        }
        if (clientOrderRequest.getAddressShipping().getWardName() != null || clientOrderRequest.getAddressShipping().getWardName().equals("")) {
            address.setWardName(clientOrderRequest.getAddressShipping().getWardName());
        }
        address.setMore(clientOrderRequest.getAddressShipping().getMore());
        address.setDistrictId(clientOrderRequest.getAddressShipping().getDistrictId());
        address.setProvinceId(clientOrderRequest.getAddressShipping().getProvinceId());
        address.setWardCode(clientOrderRequest.getAddressShipping().getWardCode());
        return clientAddressRepository.save(address);
    }


    // Tính phí ship
    public float calculateShippingFee(Address address) {
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
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod" + EntityProperties.NOT_FOUND));
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
    private List<ClientOrderHistoryResponse> createOrderHistory(Order order, OrderStatus orderStatus) {
        List<ClientOrderHistoryResponse> clientOrderHistoryResponses = new ArrayList<>();
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setActionStatus(orderStatus);
        orderHistory.setNote(order.getNote());
        orderHistory.setActionDescription(orderStatus.action_description);
        clientOrderHistoryResponses.add(ClientOrderHistoryMapper.INSTANCE.orderHistoryToClientOrderHistoryResponse(clientOrderHistoryRepository.save(orderHistory)));
        return clientOrderHistoryResponses;
    }

    // Tạo orderDetail
    private List<ClientOrderDetailResponse> createOrderDetails(Order order, ClientOrderRequest clientOrderRequest) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ClientCartItemRequest clientCartItemRequest : clientOrderRequest.getCartItems()) {
            ProductDetail productDetail = clientProductDetailRepository.findById(clientCartItemRequest.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("ProductDetail Not Found !"));
            if (productDetail != null) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProductDetail(productDetail);
                orderDetail.setQuantity(clientCartItemRequest.getQuantity());
                orderDetail.setOrder(order);
                orderDetail.setPrice(maxMoneyPromotionInProductDetail(productDetail));
                orderDetail.setTotalPrice(maxMoneyPromotionInProductDetail(productDetail) * orderDetail.getQuantity());
                orderDetails.add(orderDetail);
            }
        }
        return clientOrderDetailRepository.saveAll(orderDetails)
                .stream()
                .map(ClientOrderDetailMapper.INSTANCE::orderDetailToClientOrderDetailResponse)
                .collect(Collectors.toList());
    }

    // Tính giá sản phẩm thấp nhất trong promotionProductDetail theo value của Promotion
    private float maxMoneyPromotionInProductDetail(ProductDetail productDetail) {
        float maxPrice = 0.0f;
        List<PromotionProductDetail> promotionProductDetailsSet = productDetail.getPromotionProductDetails();
        if (promotionProductDetailsSet != null && !promotionProductDetailsSet.isEmpty()) {
            Optional<Float> maxMoneyPromotion = promotionProductDetailsSet.stream()
                    .filter(ppd -> isValid(ppd))
                    .map(ppd -> calculateMoneyPromotion(ppd))
                    .max(Float::compare);

            maxPrice = productDetail.getPrice() - maxMoneyPromotion.orElse(productDetail.getPrice());
        } else {
            maxPrice = productDetail.getPrice();
        }
        return maxPrice;
    }

    @Override
    public ClientOrderResponse findByIdAndCustomerId(String orderId, String customerId) {
        if (customerId == null) {
            Optional<Order> orderOptional = clientOrderRepository.findById(orderId);
            if (orderOptional.isEmpty()) {
                throw new ResourceNotFoundException("Order" + EntityProperties.NOT_FOUND);
            }
            return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderOptional.get());
        }
        Optional<Order> orderOptional = clientOrderRepository.findByIdAndCustomer_Id(orderId, customerId);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Order" + EntityProperties.NOT_FOUND);
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(orderOptional.get());
    }

    @Override
    public ClientOrderResponse findByCode(String code) {
        Order order = clientOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Order" + EntityProperties.NOT_FOUND));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new ResourceNotFoundException("Order tracking is expired!");
        }
        return ClientOrderMapper.INSTANCE.orderToClientOrderResponse(order);
    }

    @Override
    public Boolean cancelOrder(String code) {
        Optional<Order> orderOptional = clientOrderRepository.findByCode(code);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("Order" + EntityProperties.NOT_FOUND);
        }
        Order order = orderOptional.get();
        if (order.getStatus() != OrderStatus.WAIT_FOR_DELIVERY && order.getStatus() != OrderStatus.WAIT_FOR_CONFIRMATION) {
            throw new ApiException("Orders cannot be cancel while the status is being shipped or completed !");
        }
        if (order.getVoucher() != null) {
            Voucher voucher = order.getVoucher();
            voucher.setQuantity(voucher.getQuantity() + 1);
            clientVoucherRepository.save(voucher);
        }
        revertQuantityProductDetail(order);
        order.setStatus(OrderStatus.CANCELED);
        Order newOrder = clientOrderRepository.save(order);
        createOrderHistory(newOrder, OrderStatus.CANCELED);

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
