package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminCustomerVoucherMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminPromotionProductDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminPromotionProductDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.EntityUtil;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPromotionProductDetailServiceImpl implements AdminPromotionProductDetailService {

    private final AdminPromotionProductDetailRepository adminPromotionProductDetailRepository;
    private final PaginationUtil paginationUtil;
    private final AdminCustomerRepository adminCustomerRepository;
    private final AdminVoucherRepository adminVoucherRepository;
    private final MessageUtil messageUtil;
    private final EntityUtil entityUtil;
    private final AdminPromotionRepository adminPromotionRepository;


    @Override
    public PageableObject<AdminPromotionProductDetailResponse> findAllEntity(AdminPromotionProductDetailRequest PromotionProductDetailRequest) {
        return null;
    }

    @Override
    public Object create(AdminPromotionProductDetailRequest PromotionProductDetailRequest) {
        return null;
    }

    @Override
    public AdminPromotionProductDetailResponse update(AdminPromotionProductDetailRequest PromotionProductDetailRequest) {
        return null;
    }

    @Override
    public AdminPromotionProductDetailResponse findById(String id) {
        Optional<PromotionProductDetail> optionalPromotionProductDetail = adminPromotionProductDetailRepository.findById(id);
        if (optionalPromotionProductDetail.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("promotion.product.notfound"));
        }
        return AdminPromotionProductDetailMapper.INSTANCE.promotionProductDetailToAdminPromotionProductDetailResponse(optionalPromotionProductDetail.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<PromotionProductDetail> optionalPromotionProductDetail = adminPromotionProductDetailRepository.findById(id);
        if (optionalPromotionProductDetail.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("promotion.product.notfound"));
        }
        PromotionProductDetail newPromotionProductDetail = optionalPromotionProductDetail.get();
        newPromotionProductDetail.setDeleted(true);
        adminPromotionProductDetailRepository.save(newPromotionProductDetail);
        return true;
    }

    @Override
    public List<AdminPromotionProductDetailResponse> createPromotionProductDetail(String promotionId, List<String> productDetailIs) {
        List<PromotionProductDetail> promotionProductDetails = new ArrayList<>();
        Promotion promotion = adminPromotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("promotion.notfound")));
        if (productDetailIs != null) {
            promotionProductDetails = entityUtil.addProductDetailsToPromotion(promotion, productDetailIs);
        } else {
            throw new ResourceNotFoundException(messageUtil.getMessage("product.product_detail.notfound"));
        }
        return promotionProductDetails
                .stream()
                .map(AdminPromotionProductDetailMapper.INSTANCE::promotionProductDetailToAdminPromotionProductDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteProductDetailsByPromotionId(String promotion, List<String> productDetails) {
        adminPromotionProductDetailRepository.deleteProductDetailsByPromotionId(promotion, productDetails);
        return true;
    }

}
