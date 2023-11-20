package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminPromotionProductDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminPromotionProductDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.voucher.AdminPromotionProductDetailMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminPromotionProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.voucher.AdminVoucherRepository;
import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminPromotionProductDetailService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.voucher.PromotionProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminPromotionProductDetailServiceImpl implements AdminPromotionProductDetailService {

    private AdminPromotionProductDetailRepository adminPromotionProductDetailRepository;
    private PaginationUtil paginationUtil;
    private AdminCustomerRepository adminCustomerRepository;
    private AdminVoucherRepository adminVoucherRepository;

    @Autowired
    public AdminPromotionProductDetailServiceImpl(AdminPromotionProductDetailRepository adminPromotionProductDetailRepository,
                                                  PaginationUtil paginationUtil,
                                                  AdminCustomerRepository adminCustomerRepository,
                                                  AdminVoucherRepository adminVoucherRepository) {
        this.adminPromotionProductDetailRepository = adminPromotionProductDetailRepository;
        this.paginationUtil = paginationUtil;
        this.adminCustomerRepository = adminCustomerRepository;
        this.adminVoucherRepository = adminVoucherRepository;
    }

    @Override
    public PageableObject<AdminPromotionProductDetailResponse> findAllEntity(AdminPromotionProductDetailRequest PromotionProductDetailRequest) {
        return null;
    }

    @Override
    public AdminPromotionProductDetailResponse create(AdminPromotionProductDetailRequest PromotionProductDetailRequest) {
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
            throw new ResourceNotFoundException("PromotionProductDetail IS NOT EXIST :" + id);
        }
        return AdminPromotionProductDetailMapper.INSTANCE.promotionProductDetailToAdminPromotionProductDetailResponse(optionalPromotionProductDetail.get());
    }

    @Override
    public Boolean delete(String id) {
        Optional<PromotionProductDetail> optionalPromotionProductDetail = adminPromotionProductDetailRepository.findById(id);
        if (optionalPromotionProductDetail.isEmpty()) {
            throw new ResourceNotFoundException("PromotionProductDetail NOT FOUND :" + id);
        }
        PromotionProductDetail newPromotionProductDetail = optionalPromotionProductDetail.get();
        newPromotionProductDetail.setDeleted(true);
        adminPromotionProductDetailRepository.save(newPromotionProductDetail);
        return true;
    }

    @Override
    public Boolean deleteProductDetailsByPromotionId(String promotion, List<String> productDetails) {
        adminPromotionProductDetailRepository.deleteProductDetailsByPromotionId(promotion, productDetails);
        return true;
    }
}
