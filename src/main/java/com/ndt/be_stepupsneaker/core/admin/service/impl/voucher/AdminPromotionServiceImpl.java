package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminPromotionMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminPromotionService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminPromotionServiceImpl implements AdminPromotionService {
    @Autowired
    private CloudinaryUpload cloudinaryUpload;

    private AdminPromotionRepository adminPromotionRepository;
    private PaginationUtil paginationUtil;
    private AdminProductDetailRepository adminProductDetailRepository;
    private AdminPromotionProductDetailRepository adminPromotionProductDetailRepository;

    @Autowired
    public AdminPromotionServiceImpl(
            AdminPromotionRepository adminPromotionRepository,
            PaginationUtil paginationUtil,
            AdminProductDetailRepository adminProductDetailRepository,
            AdminPromotionProductDetailRepository adminPromotionProductDetailRepository
    ) {
        this.adminPromotionRepository = adminPromotionRepository;
        this.paginationUtil = paginationUtil;
        this.adminProductDetailRepository = adminProductDetailRepository;
        this.adminPromotionProductDetailRepository = adminPromotionProductDetailRepository;
    }


    @Override
    public PageableObject<AdminPromotionResponse> findAllEntity(AdminPromotionRequest request) {
        return null;
    }

    @Override
    public PageableObject<AdminPromotionResponse> findAllPromotion(AdminPromotionRequest request, String productDetail, String noProductDetail) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<Promotion> promotionPage = adminPromotionRepository.findAllPromotion(request, pageable, request.getStatus(), productDetail, noProductDetail);
        Page<AdminPromotionResponse> responsePage = promotionPage.map(AdminPromotionMapper.INSTANCE::promotionToAdminPromotionResponse);
        return new PageableObject<>(responsePage);

    }

    @Override
    public AdminPromotionResponse create(AdminPromotionRequest request) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findByCode(request.getCode());
        if (promotionOptional.isPresent()) {
            throw new ApiException("CODE IS EXIST");
        }
        request.setImage(cloudinaryUpload.upload(request.getImage()));
        Promotion promotion = adminPromotionRepository.save(AdminPromotionMapper.INSTANCE.adminPromotionRequestToPromotion(request));
        productDetailsInPromotion(promotion, request.getProductDetailIds());
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotion);
    }

    @Override
    public AdminPromotionResponse update(AdminPromotionRequest request) {
        Optional<Promotion> optionalPromotion = adminPromotionRepository.findByCode(request.getId(), request.getCode());
        if (optionalPromotion.isPresent()) {
            throw new ApiException("CODE IS EXIST");
        }

        optionalPromotion = adminPromotionRepository.findById(request.getId());
        if (optionalPromotion.isEmpty()) {
            throw new ResourceNotFoundException("VOUCHER IS NOT EXIST");
        }
        Promotion newPromotion = optionalPromotion.get();
        newPromotion.setName(request.getName());
        newPromotion.setCode(request.getCode());
        newPromotion.setStatus(request.getStatus());
        newPromotion.setImage(cloudinaryUpload.upload(request.getImage()));
        newPromotion.setEndDate(request.getEndDate());
        newPromotion.setStartDate(request.getStartDate());
        newPromotion.setValue(request.getValue());
        productDetailsInPromotion(newPromotion, request.getProductDetailIds());
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(adminPromotionRepository.save(newPromotion));
    }

    @Override
    public AdminPromotionResponse findById(String id) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findById(id);
        if (promotionOptional.isEmpty()) {
            throw new ResourceNotFoundException("PROMOTION IS NOT EXIST :" + id);
        }

        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotionOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findById(id);
        if (promotionOptional.isEmpty()) {
            throw new ResourceNotFoundException("PROMOTION NOT FOUND :" + id);
        }
        Promotion newPromotion = promotionOptional.get();
        newPromotion.setDeleted(true);
        adminPromotionRepository.save(newPromotion);
        return true;
    }

    public void productDetailsInPromotion(Promotion model, List<String> productDetailIds) {
        Promotion promotion = adminPromotionRepository.findById(model.getId())
                .orElseThrow(() -> new ResourceNotFoundException("PROMOTION NOT FOUND"));
        if (promotion.getPromotionProductDetailList() == null) {
            promotion.setPromotionProductDetailList(new ArrayList<>());
        }
        if (productDetailIds != null) {
            List<ProductDetail> productDetails = adminProductDetailRepository.findAllById(productDetailIds);
            List<PromotionProductDetail> promotionProductDetails = productDetails
                    .stream()
                    .map(productDetail -> {
                        PromotionProductDetail promotionProductDetail = new PromotionProductDetail();
                        promotionProductDetail.setPromotion(promotion);
                        promotionProductDetail.setProductDetail(productDetail);
                        return promotionProductDetail;
                    })
                    .collect(Collectors.toList());
            adminPromotionProductDetailRepository.saveAll(promotionProductDetails);
            promotion.getPromotionProductDetailList().addAll(promotionProductDetails);
        }
    }
}
