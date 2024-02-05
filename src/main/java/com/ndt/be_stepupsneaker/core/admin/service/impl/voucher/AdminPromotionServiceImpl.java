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
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.scheduled.ScheduledService;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.ConvertUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminPromotionServiceImpl implements AdminPromotionService {
    private CloudinaryUpload cloudinaryUpload;
    private ScheduledService scheduledService;
    private AdminPromotionRepository adminPromotionRepository;
    private PaginationUtil paginationUtil;
    private AdminProductDetailRepository adminProductDetailRepository;
    private AdminPromotionProductDetailRepository adminPromotionProductDetailRepository;

    @Autowired
    public AdminPromotionServiceImpl(CloudinaryUpload cloudinaryUpload,
                                     ScheduledService scheduledService,
                                     AdminPromotionRepository adminPromotionRepository,
                                     PaginationUtil paginationUtil,
                                     AdminProductDetailRepository adminProductDetailRepository,
                                     AdminPromotionProductDetailRepository adminPromotionProductDetailRepository) {
        this.cloudinaryUpload = cloudinaryUpload;
        this.scheduledService = scheduledService;
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
    public Object create(AdminPromotionRequest request) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findByCode(request.getCode());
        if (promotionOptional.isPresent()) {
            throw new ApiException("Code" + EntityProperties.IS_EXIST);
        }
        request.setImage(cloudinaryUpload.upload(request.getImage()));
        Promotion promotion = adminPromotionRepository.save(AdminPromotionMapper.INSTANCE.adminPromotionRequestToPromotion(request));
        adminPromotionRepository.updateStatusBasedOnTime(promotion.getId(), promotion.getStartDate(), promotion.getEndDate());
        productDetailsInPromotion(promotion, request.getProductDetailIds());
        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotion);
    }

    @Override
    public AdminPromotionResponse update(AdminPromotionRequest request) {
        Optional<Promotion> optionalPromotion = adminPromotionRepository.findByCode(request.getId(), request.getCode());
        if (optionalPromotion.isPresent()) {
            throw new ApiException("Code" + EntityProperties.IS_EXIST);
        }

        optionalPromotion = adminPromotionRepository.findById(request.getId());
        if (optionalPromotion.isEmpty()) {
            throw new ResourceNotFoundException("Promotion" + EntityProperties.NOT_FOUND);
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
            throw new ResourceNotFoundException("Promotion" + EntityProperties.NOT_FOUND);
        }

        return AdminPromotionMapper.INSTANCE.promotionToAdminPromotionResponse(promotionOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<Promotion> promotionOptional = adminPromotionRepository.findById(id);
        if (promotionOptional.isEmpty()) {
            throw new ResourceNotFoundException("Promotion" + EntityProperties.NOT_FOUND);
        }
        Promotion newPromotion = promotionOptional.get();
        newPromotion.setDeleted(true);
        adminPromotionRepository.save(newPromotion);
        return true;
    }

    public void productDetailsInPromotion(Promotion model, List<String> productDetailIds) {
        Promotion promotion = adminPromotionRepository.findById(model.getId())
                .orElseThrow(() -> new ResourceNotFoundException("PROMOTION" + EntityProperties.NOT_FOUND));
        if (promotion.getPromotionProductDetailList() == null) {
            promotion.setPromotionProductDetailList(new ArrayList<>());
        }
        if (productDetailIds != null) {
            List<ProductDetail> productDetails = adminProductDetailRepository.findAllById(productDetailIds);
            Set<ProductDetail> productDetailsToUpdate = new HashSet<>();
            List<PromotionProductDetail> promotionProductDetailsAdd = new ArrayList<>();

            List<PromotionProductDetail> promotionProductDetails = productDetails
                    .stream()
                    .map(productDetail -> {
                        PromotionProductDetail promotionProductDetail = new PromotionProductDetail();
                        promotionProductDetail.setPromotion(promotion);
                        promotionProductDetail.setProductDetail(productDetail);

                        promotionProductDetailsAdd.add(promotionProductDetail);
                        if (!productDetailsToUpdate.contains(productDetail)) {
                            Float currentMoneyPromotion = productDetail.getMoneyPromotion();

                            Optional<Float> maxMoneyPromotion = promotionProductDetail.getProductDetail().getPromotionProductDetails().stream()
                                    .filter(ppd -> isValid(ppd))
                                    .map(ppd -> calculateMoneyPromotion(ppd))
                                    .max(Float::compare);
                            System.out.println("ProductDetail ID: " + productDetail.getId());

                            if (maxMoneyPromotion.isPresent() && (currentMoneyPromotion == null || maxMoneyPromotion.get() > currentMoneyPromotion)) {
                                productDetail.setMoneyPromotion(maxMoneyPromotion.get());
                                System.out.println("Max Money Promotion: " + maxMoneyPromotion.orElse(null));
                            }

                            productDetailsToUpdate.add(productDetail);
                        }
                        return promotionProductDetail;
                    })
                    .collect(Collectors.toList());
            adminPromotionProductDetailRepository.saveAll(promotionProductDetailsAdd);
            promotion.getPromotionProductDetailList().addAll(promotionProductDetails);
            adminProductDetailRepository.saveAll(productDetailsToUpdate);

        }

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
