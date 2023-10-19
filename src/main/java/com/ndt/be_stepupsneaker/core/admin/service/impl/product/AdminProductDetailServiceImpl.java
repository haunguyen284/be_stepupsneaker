package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminProductDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductDetailService;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class AdminProductDetailServiceImpl implements AdminProductDetailService {
    @Autowired
    private AdminProductDetailRepository adminProductDetailRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    @Override
    public PageableObject<AdminProductDetailResponse> findAllEntity(AdminProductDetailRequest brandRequest) {

        Pageable pageable = paginationUtil.pageable(brandRequest);
        Page<ProductDetail> resp = adminProductDetailRepository.findAllProductDetail(brandRequest, brandRequest.getStatus(), pageable);

        Page<AdminProductDetailResponse> adminProductDetailResponses = resp.map(AdminProductDetailMapper.INSTANCE::productDetailToAdminProductDetailResponse);
        return new PageableObject<>(adminProductDetailResponses);
    }

    @Override
    public AdminProductDetailResponse create(AdminProductDetailRequest request) {
        ProductDetail productDetail =  adminProductDetailRepository.save(AdminProductDetailMapper.INSTANCE.adminProductDetailRequestToProductDetail(request));
        return AdminProductDetailMapper.INSTANCE.productDetailToAdminProductDetailResponse(productDetail);
    }


    @Override
    public AdminProductDetailResponse update(AdminProductDetailRequest brandRequest) {
        Optional<ProductDetail> productDetailOptional = adminProductDetailRepository.findById(brandRequest.getId());
        if (productDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("PRODUCT DETAIL NOT FOUND");
        }
        ProductDetail ProductDetailSave = productDetailOptional.get();
        ProductDetailSave.setStatus(brandRequest.getStatus());
        return AdminProductDetailMapper.INSTANCE.productDetailToAdminProductDetailResponse(adminProductDetailRepository.save(ProductDetailSave));
    }

    @Override
    public AdminProductDetailResponse findById(UUID id) {
        Optional<ProductDetail> ProductDetailOptional = adminProductDetailRepository.findById(id);
        if (ProductDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("BRAND IS NOT EXIST");
        }

        return AdminProductDetailMapper.INSTANCE.productDetailToAdminProductDetailResponse(ProductDetailOptional.get());
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<ProductDetail> brandOptional = adminProductDetailRepository.findById(id);
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("BRAND NOT FOUND");
        }
        ProductDetail brand = brandOptional.get();
        brand.setDeleted(true);
        adminProductDetailRepository.save(brand);
        return true;
    }
}