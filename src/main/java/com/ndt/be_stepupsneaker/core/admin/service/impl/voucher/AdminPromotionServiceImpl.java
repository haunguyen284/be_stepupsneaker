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
    private final ScheduledService scheduledService;
    private final AdminPromotionRepository adminPromotionRepository;
    private final PaginationUtil paginationUtil;
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final AdminPromotionProductDetailRepository adminPromotionProductDetailRepository;
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
    public Object create(AdminPromotionRequest request) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findByCode(request.getCode());
        if (promotionOptional.isPresent()) {
            throw new ApiException(messageUtil.getMessage("promotion.code.exist"));
        }
        request.setImage(cloudinaryUpload.upload(request.getImage()));
        Promotion promotion = adminPromotionRepository.save(AdminPromotionMapper.INSTANCE.adminPromotionRequestToPromotion(request));
        adminPromotionRepository.updateStatusBasedOnTime(promotion.getId(), promotion.getStartDate(), promotion.getEndDate());
        if (promotion != null && request.getProductDetailIds() != null) {
            entityUtil.addProductDetailsToPromotion(promotion,request.getProductDetailIds());
        }
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotion);
    }

    @Override
    public AdminPromotionResponse update(AdminPromotionRequest request) {
        Optional<Promotion> optionalPromotion = adminPromotionRepository.findByCode(request.getId(), request.getCode());
        if (optionalPromotion.isPresent()) {
            throw new ApiException(messageUtil.getMessage("promotion.code.exist"));
        }

        optionalPromotion = adminPromotionRepository.findById(request.getId());
        if (optionalPromotion.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("promotion.notfound"));
        }
        Promotion newPromotion = optionalPromotion.get();
        newPromotion.setName(request.getName());
        newPromotion.setCode(request.getCode());
        newPromotion.setStatus(request.getStatus());
        newPromotion.setImage(cloudinaryUpload.upload(request.getImage()));
        newPromotion.setEndDate(request.getEndDate());
        newPromotion.setStartDate(request.getStartDate());
        newPromotion.setValue(request.getValue());
        ;
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(adminPromotionRepository.save(newPromotion));
    }

    @Override
    public AdminPromotionResponse findById(String id) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findById(id);
        if (promotionOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("promotion.notfound"));
        }

        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotionOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findById(id);
        if (promotionOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("promotion.notfound"));
        }
        Promotion newPromotion = promotionOptional.get();
        newPromotion.setDeleted(true);
        adminPromotionRepository.save(newPromotion);
        return true;
    }


    public boolean isValid(PromotionProductDetail ppd) {
        Promotion promotion = ppd.getPromotion();
        if (promotion != null && promotion.getEndDate() != null) {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime endDate = ConvertUtil.convertLongToLocalDateTime(promotion.getEndDate());
            return !currentDate.isAfter(endDate);
        }
        return false;
    }

    // Phương thức tính toán giá trị moneyPromotion
    public Float calculateMoneyPromotion(PromotionProductDetail ppd) {
        Float promotionMoney = (ppd.getProductDetail().getPrice() * ppd.getPromotion().getValue()) / 100;
        return promotionMoney;
    }
}
