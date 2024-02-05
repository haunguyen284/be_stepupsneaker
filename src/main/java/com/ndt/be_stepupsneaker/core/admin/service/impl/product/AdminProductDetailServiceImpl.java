package com.ndt.be_stepupsneaker.core.admin.service.impl.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminProductDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminBrandRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminColorRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminMaterialRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminSizeRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminSoleRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminStyleRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminTradeMarkRepository;
import com.ndt.be_stepupsneaker.core.admin.service.product.AdminProductDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AdminProductDetailServiceImpl implements AdminProductDetailService {

    @Autowired
    private CloudinaryUpload cloudinaryUpload;

    @Autowired
    private AdminProductDetailRepository adminProductDetailRepository;

    @Autowired
    private AdminTradeMarkRepository adminTradeMarkRepository;

    @Autowired
    private AdminStyleRepository adminStyleRepository;

    @Autowired
    private AdminSizeRepository adminSizeRepository;

    @Autowired
    private AdminProductRepository adminProductRepository;

    @Autowired
    private AdminMaterialRepository adminMaterialRepository;

    @Autowired
    private AdminColorRepository adminColorRepository;

    @Autowired
    private AdminBrandRepository adminBrandRepository;

    @Autowired
    private AdminSoleRepository adminSoleRepository;

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
    public Object create(AdminProductDetailRequest request) {
        return null;
    }

    @Override
    public List<AdminProductDetailResponse> create(List<AdminProductDetailRequest> productDetailRequests) {
        List<ProductDetail> productDetails = new ArrayList<>();
        for (AdminProductDetailRequest adminProductDetailRequest: productDetailRequests) {
            adminProductDetailRequest.setImage(cloudinaryUpload.upload(adminProductDetailRequest.getImage()));
            Optional<ProductDetail> productDetailOptional = adminProductDetailRepository.findByProductProperties(adminProductDetailRequest);
            if (productDetailOptional.isPresent()) {
                ProductDetail productDetail = productDetailOptional.get();
                productDetail.setQuantity(productDetail.getQuantity() + adminProductDetailRequest.getQuantity());
                productDetail.setImage(adminProductDetailRequest.getImage());
                productDetail.setPrice(adminProductDetailRequest.getPrice());
                productDetails.add(productDetail);
            } else {
                productDetails.add(AdminProductDetailMapper.INSTANCE.adminProductDetailRequestToProductDetail(adminProductDetailRequest));
            }
        }
        return adminProductDetailRepository.saveAll(productDetails).stream().map(AdminProductDetailMapper.INSTANCE::productDetailToAdminProductDetailResponse).collect(Collectors.toList());
    }

    @Override
    public List<AdminProductDetailResponse> update(List<AdminProductDetailRequest> productDetailRequests) {
        List<ProductDetail> productDetails = new ArrayList<>();
        for (AdminProductDetailRequest adminProductDetailRequest: productDetailRequests) {
            adminProductDetailRequest.setImage(cloudinaryUpload.upload(adminProductDetailRequest.getImage()));
            productDetails.add(AdminProductDetailMapper.INSTANCE.adminProductDetailRequestToProductDetail(adminProductDetailRequest));
        }
        return adminProductDetailRepository.saveAll(productDetails).stream().map(AdminProductDetailMapper.INSTANCE::productDetailToAdminProductDetailResponse).collect(Collectors.toList());
    }

    @Override
    public PageableObject<AdminProductDetailResponse> findByTrending(Long fromDate, Long toDate) {
        List<ProductDetail> productDetails = adminProductDetailRepository.findProductDetailTrending(fromDate, toDate);
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<ProductDetail> resp = new PageImpl<>(productDetails, pageRequest, productDetails.size());
        Page<AdminProductDetailResponse> adminProductDetailResponses = resp.map(AdminProductDetailMapper.INSTANCE::productDetailToAdminProductDetailResponse);
        return new PageableObject<>(adminProductDetailResponses);
    }


    @Override
    public AdminProductDetailResponse update(AdminProductDetailRequest productDetailRequest) {
        Optional<ProductDetail> productDetailOptional = adminProductDetailRepository.findById(productDetailRequest.getId());
        if (productDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("PRODUCT DETAIL NOT FOUND");
        }

        ProductDetail productDetailSave = productDetailOptional.get();

        productDetailSave.setTradeMark(adminTradeMarkRepository.findById(productDetailRequest.getTradeMark()).orElse(null));
        productDetailSave.setStyle(adminStyleRepository.findById(productDetailRequest.getStyle()).orElse(null));
        productDetailSave.setSize(adminSizeRepository.findById(productDetailRequest.getSize()).orElse(null));
        productDetailSave.setProduct(adminProductRepository.findById(productDetailRequest.getProduct()).orElse(null));
        productDetailSave.setMaterial(adminMaterialRepository.findById(productDetailRequest.getMaterial()).orElse(null));
        productDetailSave.setColor(adminColorRepository.findById(productDetailRequest.getColor()).orElse(null));
        productDetailSave.setBrand(adminBrandRepository.findById(productDetailRequest.getBrand()).orElse(null));
        productDetailSave.setSole(adminSoleRepository.findById(productDetailRequest.getSole()).orElse(null));
        productDetailSave.setImage(productDetailRequest.getImage());
        productDetailSave.setPrice(productDetailRequest.getPrice());
        productDetailSave.setQuantity(productDetailRequest.getQuantity());
        productDetailSave.setStatus(productDetailRequest.getStatus());
        return AdminProductDetailMapper.INSTANCE.productDetailToAdminProductDetailResponse(adminProductDetailRepository.save(productDetailSave));
    }

    @Override
    public AdminProductDetailResponse findById(String id) {
        Optional<ProductDetail> ProductDetailOptional = adminProductDetailRepository.findById(id);
        if (ProductDetailOptional.isEmpty()){
            throw new ResourceNotFoundException("PRODUCT DETAIL IS NOT EXIST");
        }

        return AdminProductDetailMapper.INSTANCE.productDetailToAdminProductDetailResponse(ProductDetailOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<ProductDetail> brandOptional = adminProductDetailRepository.findById(id);
        if (brandOptional.isEmpty()){
            throw new ResourceNotFoundException("PRODUCT DETAIL NOT FOUND");
        }
        ProductDetail brand = brandOptional.get();
        brand.setDeleted(true);
        adminProductDetailRepository.save(brand);
        return true;
    }
}