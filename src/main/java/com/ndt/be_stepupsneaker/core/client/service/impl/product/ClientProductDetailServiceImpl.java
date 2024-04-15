package com.ndt.be_stepupsneaker.core.client.service.impl.product;

import com.ndt.be_stepupsneaker.core.client.dto.request.product.ClientProductDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.product.ClientProductDetailResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.product.ClientProductDetailMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.service.product.ClientProductDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ClientProductDetailServiceImpl implements ClientProductDetailService {

    @Autowired
    private ClientProductDetailRepository clientProductDetailRepository;


    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private MessageUtil messageUtil;

    @Override
    public PageableObject<ClientProductDetailResponse> findAllEntity(ClientProductDetailRequest request) {

        Pageable pageable = paginationUtil.pageable(request);
        Page<ProductDetail> resp = clientProductDetailRepository.findAllProductDetail(request, request.getStatus(), pageable);

        Page<ClientProductDetailResponse> adminProductDetailResponses = resp.map(ClientProductDetailMapper.INSTANCE::productDetailToClientProductDetailResponse);
        return new PageableObject<>(adminProductDetailResponses);
    }

    @Override
    public Object create(ClientProductDetailRequest request) {
        return null;
    }

    @Override
    public ClientProductDetailResponse update(ClientProductDetailRequest request) {
        return null;
    }

    @Override
    public ClientProductDetailResponse findById(String id) {
        Optional<ProductDetail> ProductDetailOptional = clientProductDetailRepository.findById(id);
        if (ProductDetailOptional.isEmpty()){
            throw new ResourceNotFoundException(messageUtil.getMessage("product.product_detail.notfound"));
        }

        return ClientProductDetailMapper.INSTANCE.productDetailToClientProductDetailResponse(ProductDetailOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }

}