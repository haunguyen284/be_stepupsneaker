package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminProductDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminOrderDetailServiceImpl implements AdminOrderDetailService {

    private final AdminProductDetailRepository adminProductDetailRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final AdminOrderDetailRepository adminOrderDetailRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    public AdminOrderDetailServiceImpl(
            AdminProductDetailRepository adminProductDetailRepository,
            AdminOrderRepository adminOrderRepository,
            AdminOrderDetailRepository adminOrderDetailRepository,
            PaginationUtil paginationUtil
    ) {
        this.adminProductDetailRepository = adminProductDetailRepository;
        this.adminOrderRepository = adminOrderRepository;
        this.adminOrderDetailRepository = adminOrderDetailRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<AdminOrderDetailResponse> findAllEntity(AdminOrderDetailRequest orderDetailRequest) {

        Pageable pageable = paginationUtil.pageable(orderDetailRequest);
        Page<OrderDetail> resp = adminOrderDetailRepository.findAllOrderDetail(orderDetailRequest, orderDetailRequest.getStatus(), pageable);

        Page<AdminOrderDetailResponse> adminOrderDetailResponses = resp.map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse);
        return new PageableObject<>(adminOrderDetailResponses);

    }

    @Override
    public AdminOrderDetailResponse create(AdminOrderDetailRequest orderDetailRequest) {
        return null;
    }

    @Override
    public AdminOrderDetailResponse update(AdminOrderDetailRequest orderDetailRequest) {
        Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(orderDetailRequest.getId());
        if(orderDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER DETAIL NOT FOUND");
        }

        OrderDetail orderDetail = orderDetailOptional.get();

        orderDetail.setOrder(adminOrderRepository.findById(orderDetailRequest.getOrder()).orElse(null));
        orderDetail.setProductDetail(adminProductDetailRepository.findById(orderDetailRequest.getProductDetail()).orElse(null));
        orderDetail.setQuantity(orderDetailRequest.getQuantity());
        orderDetail.setPrice(orderDetailRequest.getPrice());
        orderDetail.setStatus(orderDetailRequest.getStatus());
        return null;
    }

    @Override
    public List<AdminOrderDetailResponse> create(List<AdminOrderDetailRequest> orderDetailRequests) {
        List<OrderDetail> orderDetails = orderDetailRequests.stream().map(AdminOrderDetailMapper.INSTANCE::adminOrderDetailRequestToOrderDetail).collect(Collectors.toList());

        return adminOrderDetailRepository.saveAll(orderDetails).stream().map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse).collect(Collectors.toList());
    }

    @Override
    public List<AdminOrderDetailResponse> update(List<AdminOrderDetailRequest> orderDetailRequests) {
        List<OrderDetail> orderDetails = orderDetailRequests.stream().map(AdminOrderDetailMapper.INSTANCE::adminOrderDetailRequestToOrderDetail).collect(Collectors.toList());
        return adminOrderDetailRepository.saveAll(orderDetails).stream().map(AdminOrderDetailMapper.INSTANCE::orderDetailToAdminOrderDetailResponse).collect(Collectors.toList());
    }

    @Override
    public AdminOrderDetailResponse findById(UUID id) {
        Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(id);
        if(orderDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER DETAIL IS NOT EXIST");
        }

        return AdminOrderDetailMapper.INSTANCE.orderDetailToAdminOrderDetailResponse(orderDetailOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<OrderDetail> orderDetailOptional = adminOrderDetailRepository.findById(id);
        if(orderDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER DETAIL IS NOT EXIST");
        }

        OrderDetail orderDetail = orderDetailOptional.get();
        orderDetail.setDeleted(true);
        adminOrderDetailRepository.save(orderDetail);
        return true;
    }

}
