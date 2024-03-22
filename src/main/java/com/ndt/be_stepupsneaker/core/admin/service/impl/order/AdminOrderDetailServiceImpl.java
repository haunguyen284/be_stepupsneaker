package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.OrderUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderDetailServiceImpl implements AdminOrderDetailService {

    private final AdminProductDetailRepository adminProductDetailRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final PaginationUtil paginationUtil;
    private final MessageUtil messageUtil;
    private final OrderUtil orderUtil;


    @Override
    public PageableObject<AdminOrderDetailResponse> findAllEntity(AdminOrderDetailRequest orderDetailRequest) {

        Pageable pageable = paginationUtil.pageable(orderDetailRequest);
        Page<OrderDetail> resp = adminOrderDetailRepository.findAllOrderDetail(orderDetailRequest, orderDetailRequest.getStatus(), pageable);

        Page<AdminOrderDetailResponse> adminOrderDetailResponses = resp.map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse);
        return new PageableObject<>(adminOrderDetailResponses);

    }

    @Override
    public Object create(AdminOrderDetailRequest orderDetailRequest) {
        return null;
    }

    @Override
    public AdminOrderDetailResponse update(AdminOrderDetailRequest orderDetailRequest) {
        Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(orderDetailRequest.getId());
        Optional<Order> orderOptional = adminOrderRepository.findById(orderDetailRequest.getOrder());
        if (orderDetailOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
        }
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.notfound"));
        }

        OrderDetail orderDetail = orderDetailOptional.get();
        int totalQuantity = 0;
        orderDetail.setOrder(orderOptional.get());
        orderDetail.setProductDetail(adminProductDetailRepository.findById(orderDetailRequest.getProductDetail()).orElse(null));
        ProductDetail productDetail = orderDetail.getProductDetail();
        if (productDetail.getQuantity() < orderDetail.getQuantity()) {
            throw new ApiException(messageUtil.getMessage("order.not_enough_quantity"));
        }
        if (orderDetailRequest.getQuantity() > orderDetail.getQuantity()) {
            totalQuantity = orderDetailRequest.getQuantity() - orderDetail.getQuantity();
            productDetail.setQuantity(productDetail.getQuantity() - totalQuantity);
        } else {
            totalQuantity = orderDetail.getQuantity() - orderDetailRequest.getQuantity();
            productDetail.setQuantity(productDetail.getQuantity() + totalQuantity);
        }
        float promotionValue = orderUtil.getPromotionValueOfProductDetail(productDetail);
        orderDetail.setQuantity(orderDetailRequest.getQuantity());
        orderDetail.setPrice(productDetail.getPrice() - promotionValue);
        orderDetail.setTotalPrice(orderDetail.getQuantity() * orderDetail.getPrice());
        orderDetail.setStatus(orderDetailRequest.getStatus());

        adminProductDetailRepository.save(productDetail);
        adminOrderDetailRepository.save(orderDetail);
        return AdminOrderDetailMapper.INSTANCE.orderDetailToAdminOrderDetailResponse(orderDetail);
    }

    @Override
    public List<AdminOrderDetailResponse> create(List<AdminOrderDetailRequest> orderDetailRequests) {
        List<ProductDetail> productDetails = new ArrayList<>();
        List<OrderDetail> addOrderDetails = new ArrayList<>();
        for (AdminOrderDetailRequest orderDetailRequest : orderDetailRequests) {
            List<OrderDetail> orderDetails = adminOrderDetailRepository.findAllByOrder_Id(orderDetailRequest.getOrder());
            ProductDetail productDetail = adminProductDetailRepository.findById(orderDetailRequest.getProductDetail())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.notfound")));
            if (orderDetailRequest.getQuantity() > productDetail.getQuantity()) {
                throw new ApiException(messageUtil.getMessage("order.not_enough_quantity"));
            }
            for (OrderDetail detail : orderDetails) {
                if (detail.getProductDetail().getId() == productDetail.getId()) {
                    int totalQuantity = detail.getQuantity() + orderDetailRequest.getQuantity();
                    if (detail.getProductDetail().getQuantity() < totalQuantity) {
                        throw new ApiException(messageUtil.getMessage("order.not_enough_quantity"));
                    }
                    float promotionValue = orderUtil.getPromotionValueOfProductDetail(detail.getProductDetail());
                    detail.setQuantity(totalQuantity);
                    detail.setTotalPrice(totalQuantity * (detail.getProductDetail().getPrice() - promotionValue));
                    productDetail.setQuantity(productDetail.getQuantity() - orderDetailRequest.getQuantity());
                    adminProductDetailRepository.save(productDetail);
                    addOrderDetails.add(detail);
                    orderDetailRequests.remove(orderDetailRequest);
                    List<AdminOrderDetailResponse> adminOrderDetailResponse = adminOrderDetailRepository.saveAll(addOrderDetails)
                            .stream()
                            .map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse)
                            .collect(Collectors.toList());
                    addOrderDetails.clear();
                    return adminOrderDetailResponse;
                }

            }

            productDetail.setQuantity(productDetail.getQuantity() - orderDetailRequest.getQuantity());
            productDetails.add(productDetail);
            OrderDetail newOrderDetail = AdminOrderDetailMapper.INSTANCE.adminOrderDetailRequestToOrderDetail(orderDetailRequest);
            ProductDetail productDetail1 = adminProductDetailRepository.findById(orderDetailRequest.getProductDetail())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.product_detail.notfound")));
            float promotionValue = orderUtil.getPromotionValueOfProductDetail(productDetail1);

            newOrderDetail.setTotalPrice((productDetail1.getPrice() - promotionValue) * orderDetailRequest.getQuantity());
            newOrderDetail.setPrice(productDetail1.getPrice() - promotionValue);
            addOrderDetails.add(newOrderDetail);
        }
        adminProductDetailRepository.saveAll(productDetails);
        return adminOrderDetailRepository.saveAll(addOrderDetails).stream().map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse).collect(Collectors.toList());
    }

    @Override
    public List<AdminOrderDetailResponse> update(List<AdminOrderDetailRequest> orderDetailRequests) {
        List<OrderDetail> orderDetails = orderDetailRequests.stream().map(AdminOrderDetailMapper.INSTANCE::adminOrderDetailRequestToOrderDetail).collect(Collectors.toList());
        return adminOrderDetailRepository.saveAll(orderDetails).stream().map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse).collect(Collectors.toList());
    }

    @Override
    public AdminOrderDetailResponse findById(String id) {
        Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(id);
        if (orderDetailOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
        }

        return AdminOrderDetailMapper.INSTANCE.orderDetailToAdminOrderDetailResponse(orderDetailOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(id);
        if (orderDetailOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("order.order_detail.notfound"));
        }
        OrderDetail orderDetail = orderDetailOptional.get();
        ProductDetail productDetail = orderDetail.getProductDetail();
        productDetail.setQuantity(orderDetail.getQuantity() + productDetail.getQuantity());
        adminProductDetailRepository.save(productDetail);
        orderDetail.setDeleted(true);
        adminOrderDetailRepository.delete(orderDetail);
        return true;
    }

}
