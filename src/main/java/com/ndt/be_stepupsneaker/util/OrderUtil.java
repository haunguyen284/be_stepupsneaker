package com.ndt.be_stepupsneaker.util;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminVoucherHistoryMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.payment.AdminPaymentRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherHistoryRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientShippingRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientShippingDataResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientShippingResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderHistoryMapper;
import com.ndt.be_stepupsneaker.core.client.mapper.voucher.ClientVoucherHistoryMapper;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherType;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderUtil {

    private final AdminProductDetailRepository adminProductDetailRepository;
    private final AdminVoucherRepository adminVoucherRepository;
    private final MessageUtil messageUtil;
    private final AdminVoucherHistoryRepository adminVoucherHistoryRepository;
    private final AdminPaymentRepository adminPaymentRepository;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final AdminOrderHistoryRepository adminOrderHistoryRepository;
    private final AdminCustomerRepository adminCustomerRepository;

    //   Kiểm tra đợt giảm giá còn hạn hoặc bắt đầu hay chưa
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


    // Tính phí ship
    public float calculateShippingFee(float totalMoney, Address address) {
        if (totalMoney >= EntityProperties.IS_FREE_SHIPPING) {
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
        return 36500f;
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

    public OrderDetail createOrderDetail(ProductDetail productDetail, Order order, int quantityChange, float newProductPrice) {
        OrderDetail newOrderDetail = new OrderDetail();
        newOrderDetail.setProductDetail(productDetail);
        newOrderDetail.setQuantity(quantityChange);
        newOrderDetail.setPrice(newProductPrice);
        newOrderDetail.setTotalPrice(newProductPrice * quantityChange);
        newOrderDetail.setOrder(order);
        return newOrderDetail;
    }

    // Revert lại số lượng ProductDetail khi xóa OrderDetail
    public void revertQuantityProductDetailWhenRemoveOrderDetail(List<OrderDetail> orderDetails) {
        List<ProductDetail> productDetails = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
            productDetails.add(productDetail);
        }
        adminProductDetailRepository.saveAll(productDetails);
    }

    // Thêm khuyến mãi vào order
    public void applyVoucherToOrder(Order order, String voucherId, float totalOrderPrice, float shippingFee, String action) {
        if (voucherId != null && !voucherId.isBlank()) {
            Voucher voucher = adminVoucherRepository.findById(voucherId)
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("voucher.notfound")));
            if (voucher != null) {
                if (voucher.getConstraint() > totalOrderPrice) {
                    throw new ResourceNotFoundException(messageUtil.getMessage("order.eligible_voucher"));
                }
                if (action.equals("add")) {
                    if (voucher.getQuantity() < 1) {
                        throw new ResourceNotFoundException(messageUtil.getMessage("voucher.expired"));
                    }
                    updateQuantityVoucher(voucher, voucher.getQuantity() - 1);
                } else {
                    if (order.getVoucher() != null) {
                        if (!voucherId.equals(order.getVoucher().getId())) {
                            if (voucher.getQuantity() < 1) {
                                throw new ResourceNotFoundException(messageUtil.getMessage("voucher.expired"));
                            }
                            Voucher voucherOld = order.getVoucher();
                            updateQuantityVoucher(voucherOld, voucherOld.getQuantity() + 1);
                            updateQuantityVoucher(voucher, voucher.getQuantity() - 1);
                        }
                    } else {
                        if (voucher.getQuantity() < 1) {
                            throw new ResourceNotFoundException(messageUtil.getMessage("voucher.expired"));
                        }
                        updateQuantityVoucher(voucher, voucher.getQuantity() - 1);
                    }
                }
                order.setVoucher(voucher);
                float discount = voucher.getType() == VoucherType.CASH ? voucher.getValue() : (voucher.getValue() / 100) * totalOrderPrice;
                float finalTotalPrice = Math.max(0, totalOrderPrice - discount);
                order.setReduceMoney(discount);
                order.setTotalMoney(finalTotalPrice + shippingFee);
                // update lại voucherHistory
                if (order.getVoucherHistories() != null && action.equals("update") && !order.getVoucherHistories().isEmpty()) {
                    VoucherHistory voucherHistory = adminVoucherHistoryRepository
                            .findById(order.getVoucherHistories().get(0).getId())
                            .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("voucher.history.notfound")));

                    float totalMoney = order.getOriginMoney();
                    float voucherValue = order.getVoucher().getValue();
                    float reduceMoney = order.getVoucher().getType() == VoucherType.CASH ? voucherValue : ((totalMoney * voucherValue) / 100);
                    voucherHistory.setVoucher(order.getVoucher());
                    voucherHistory.setVoucher(voucher);
                    voucherHistory.setMoneyReduction(reduceMoney);
                    voucherHistory.setMoneyBeforeReduction(totalMoney);
                    voucherHistory.setMoneyAfterReduction(order.getOriginMoney() - order.getReduceMoney());
                    AdminVoucherHistoryMapper.INSTANCE.voucherHistoryToAdminVoucherHistoryResponse(adminVoucherHistoryRepository.save(voucherHistory));
                } else {
                    if (action.equals("update")) {
                        createVoucherHistory(order);
                    }
                }
            }

        } else {
            order.setReduceMoney(0);
            order.setTotalMoney(totalOrderPrice + shippingFee);
            if (order.getVoucher() != null && voucherId == null) {
                Voucher voucher = order.getVoucher();
                updateQuantityVoucher(voucher, voucher.getQuantity() + 1);
                List<VoucherHistory> voucherHistories = order.getVoucherHistories();
                adminVoucherHistoryRepository.deleteAll(voucherHistories);
            }
            order.setVoucher(null);
        }
    }

    public Voucher updateQuantityVoucher(Voucher voucher, int quantity) {
        voucher.setQuantity(quantity);
        return adminVoucherRepository.save(voucher);
    }

    public ClientVoucherHistoryResponse createVoucherHistory(Order order) {
        if (order.getVoucher() != null) {
            VoucherHistory voucherHistory = new VoucherHistory();
            float totalMoney = order.getOriginMoney();
            float voucherValue = order.getVoucher().getValue();
            float reduceMoney = order.getVoucher().getType() == VoucherType.CASH ? voucherValue : ((totalMoney * voucherValue) / 100);
            voucherHistory.setVoucher(order.getVoucher());
            voucherHistory.setOrder(order);
            voucherHistory.setMoneyReduction(reduceMoney);
            voucherHistory.setMoneyBeforeReduction(totalMoney);
            voucherHistory.setMoneyAfterReduction(order.getOriginMoney() - order.getReduceMoney());
            return ClientVoucherHistoryMapper
                    .INSTANCE
                    .voucherHistoryToClientVoucherHistoryResponse(adminVoucherHistoryRepository.save(voucherHistory));
        }
        return null;
    }

    public Payment updatePayment(Order order) {
        Payment payment = order.getPayments().get(0);
        payment.setTotalMoney(order.getTotalMoney());
        return adminPaymentRepository.save(payment);
    }

    // revert lại quantity productDetail khi hủy Order
    public void revertQuantityProductDetailWhenCancelOrder(Order order) {
        List<OrderDetail> orderDetails = adminOrderDetailRepository.findAllByOrder(order);
        List<ProductDetail> productDetails = orderDetails.stream().map(orderDetail -> {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity());
            return productDetail;
        }).collect(Collectors.toList());
        adminProductDetailRepository.saveAll(productDetails);
    }

    // Tạo orderHistory
    public OrderHistory createOrderHistory(Order order, OrderStatus orderStatus, String orderHistoryNote) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setActionStatus(orderStatus);
        orderHistory.setNote(orderHistoryNote);
        return adminOrderHistoryRepository.save(orderHistory);
    }

    public Customer getCustomer(AdminOrderRequest request) {
        if (request.getCustomer() != null) {
            Customer customer = adminCustomerRepository.findById(request.getCustomer())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("customer.notfound")));
            return customer;
        } else {
            return null;
        }
    }
}
