package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminPromotionMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminPromotionService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.VoucherStatus;
import com.ndt.be_stepupsneaker.infrastructure.scheduled.ScheduledService;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPromotionServiceImpl implements AdminPromotionService {
    private final CloudinaryUpload cloudinaryUpload;
    private final AdminPromotionRepository adminPromotionRepository;
    private final PaginationUtil paginationUtil;
    private final MessageUtil messageUtil;
    private final EntityUtil entityUtil;

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
    public AdminPromotionResponse deactivateDiscount(String  id) {
        Promotion promotion = getPromotion(id);
        promotion.setStatus(VoucherStatus.CANCELLED);
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(adminPromotionRepository.save(promotion));
    }

    @Override
    public Object create(AdminPromotionRequest request) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findByCode(request.getCode());
        if (promotionOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("promotion.code.exist"));
        }
        request.setImage(cloudinaryUpload.upload(request.getImage()));
        Promotion promotion = adminPromotionRepository.save(AdminPromotionMapper.INSTANCE.adminPromotionRequestToPromotion(request));
        adminPromotionRepository.updateStatusBasedOnTime(promotion.getId(), promotion.getStartDate(), promotion.getEndDate());
        if (promotion != null && request.getProductDetailIds() != null) {
            entityUtil.addProductDetailsToPromotion(promotion, request.getProductDetailIds());
        }
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotion);
    }

    @Override
    public AdminPromotionResponse update(AdminPromotionRequest request) {
        Optional<Promotion> optionalPromotion = adminPromotionRepository.findByCode(request.getId(), request.getCode());
        if (optionalPromotion.isPresent()) {
            throw new ApiException(messageUtil.getMessage("promotion.code.exist"));
        }
        Promotion newPromotion = getPromotion(request.getId());
        newPromotion.setName(request.getName());
        newPromotion.setCode(request.getCode());
        newPromotion.setImage(cloudinaryUpload.upload(request.getImage()));
        newPromotion.setEndDate(request.getEndDate());
        newPromotion.setStartDate(request.getStartDate());
        newPromotion.setValue(request.getValue());
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(adminPromotionRepository.save(newPromotion));
    }

    @Override
    public AdminPromotionResponse findById(String id) {
        Promotion promotion = getPromotion(id);
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotion);
    }

    @Override
    public Boolean delete(String id) {
        Promotion promotion = getPromotion(id);
        promotion.setDeleted(true);
        adminPromotionRepository.save(promotion);
        return true;
    }


    private Promotion getPromotion(String id) {
        Promotion promotion = adminPromotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("promotion.notfound")));
        return promotion;
    }
}
