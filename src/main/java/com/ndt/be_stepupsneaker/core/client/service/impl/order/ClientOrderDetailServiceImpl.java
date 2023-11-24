package com.ndt.be_stepupsneaker.core.client.service.impl.order;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.order.ClientOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.service.order.ClientOrderDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientOrderDetailServiceImpl implements ClientOrderDetailService {

    private final ClientProductDetailRepository clientProductDetailRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final ClientOrderDetailRepository clientOrderDetailRepository;
    private final PaginationUtil paginationUtil;

    @Autowired
    public ClientOrderDetailServiceImpl(
            ClientProductDetailRepository clientProductDetailRepository,
            ClientOrderRepository clientOrderRepository,
            ClientOrderDetailRepository clientOrderDetailRepository,
            PaginationUtil paginationUtil
    ) {
        this.clientOrderDetailRepository = clientOrderDetailRepository;
        this.clientOrderRepository = clientOrderRepository;
        this.clientProductDetailRepository = clientProductDetailRepository;
        this.paginationUtil = paginationUtil;
    }

    @Override
    public PageableObject<ClientOrderDetailResponse> findAllEntity(ClientOrderDetailRequest orderDetailRequest) {

        Pageable pageable = paginationUtil.pageable(orderDetailRequest);
        Page<OrderDetail> resp =clientOrderDetailRepository.findAllOrderDetail(orderDetailRequest, orderDetailRequest.getStatus(), pageable);

        Page<ClientOrderDetailResponse> ClientOrderDetailResponses = resp.map(ClientOrderDetailMapper.INSTANCE::orderDetailToClientOrderDetailResponse);
        return new PageableObject<>(ClientOrderDetailResponses);

    }

    @Override
    public Object create(ClientOrderDetailRequest orderDetailRequest) {
        return null;
    }

    @Override
    public ClientOrderDetailResponse update(ClientOrderDetailRequest orderDetailRequest) {
        Optional<OrderDetail> orderDetailOptional = clientOrderDetailRepository.findById(orderDetailRequest.getId());
        if(orderDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER DETAIL NOT FOUND");
        }

        OrderDetail orderDetail = orderDetailOptional.get();

        orderDetail.setOrder(clientOrderRepository.findById(orderDetailRequest.getOrder()).orElse(null));
        orderDetail.setProductDetail(clientProductDetailRepository.findById(orderDetailRequest.getProductDetail()).orElse(null));
        orderDetail.setQuantity(orderDetailRequest.getQuantity());
        orderDetail.setPrice(orderDetailRequest.getPrice());
        orderDetail.setStatus(orderDetailRequest.getStatus());
        clientOrderDetailRepository.save(orderDetail);

        return ClientOrderDetailMapper.INSTANCE.orderDetailToClientOrderDetailResponse(orderDetail);
    }

    @Override
    public List<ClientOrderDetailResponse> create(List<ClientOrderDetailRequest> orderDetailRequests) {
        List<OrderDetail> orderDetails = orderDetailRequests.stream().map(ClientOrderDetailMapper.INSTANCE::clientOrderDetailRequestToOrderDetail).collect(Collectors.toList());
        return clientOrderDetailRepository.saveAll(orderDetails).stream().map(ClientOrderDetailMapper.INSTANCE::orderDetailToClientOrderDetailResponse).collect(Collectors.toList());
    }

    @Override
    public List<ClientOrderDetailResponse> update(List<ClientOrderDetailRequest> orderDetailRequests) {
        List<OrderDetail> orderDetails = orderDetailRequests.stream().map(ClientOrderDetailMapper.INSTANCE::clientOrderDetailRequestToOrderDetail).collect(Collectors.toList());
        return clientOrderDetailRepository.saveAll(orderDetails).stream().map(ClientOrderDetailMapper.INSTANCE::orderDetailToClientOrderDetailResponse).collect(Collectors.toList());
    }

    @Override
    public ClientOrderDetailResponse findById(String id) {
        Optional<OrderDetail> orderDetailOptional = clientOrderDetailRepository.findById(id);
        if(orderDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER DETAIL IS NOT EXIST");
        }

        return ClientOrderDetailMapper.INSTANCE.orderDetailToClientOrderDetailResponse(orderDetailOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<OrderDetail> orderDetailOptional = clientOrderDetailRepository.findById(id);
        if(orderDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("ORDER DETAIL IS NOT EXIST");
        }

        OrderDetail orderDetail = orderDetailOptional.get();
        orderDetail.setDeleted(true);
        clientOrderDetailRepository.delete(orderDetail);
        return true;
    }

}
