package com.ndt.be_stepupsneaker.core.client.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientPromotionRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientPromotionResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.voucher.ClientPromotionMapper;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.voucher.ClientPromotionRepository;
import com.ndt.be_stepupsneaker.core.client.service.voucher.ClientPromotionService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.voucher.Promotion;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.scheduled.AutoScheduledService;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClientPromotionServiceImpl implements ClientPromotionService {
    private CloudinaryUpload cloudinaryUpload;
    private AutoScheduledService autoScheduledService;
    private ClientPromotionRepository clientPromotionRepository;
    private PaginationUtil paginationUtil;
    private ClientProductDetailRepository clientProductDetailRepository;

    @Autowired
    public ClientPromotionServiceImpl(CloudinaryUpload cloudinaryUpload,
                                      AutoScheduledService autoScheduledService,
                                      ClientPromotionRepository ClientPromotionRepository,
                                      PaginationUtil paginationUtil,
                                      ClientProductDetailRepository clientProductDetailRepository) {
        this.cloudinaryUpload = cloudinaryUpload;
        this.autoScheduledService = autoScheduledService;
        this.clientPromotionRepository = ClientPromotionRepository;
        this.paginationUtil = paginationUtil;
        this.clientProductDetailRepository = clientProductDetailRepository;

    }

    @Override
    public PageableObject<ClientPromotionResponse> findAllEntity(ClientPromotionRequest request) {
        return null;
    }

    @Override
    public PageableObject<ClientPromotionResponse> findAllPromotion(ClientPromotionRequest request, String productDetail, String noProductDetail) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<Promotion> promotionPage = clientPromotionRepository.findAllPromotion(request, pageable, request.getStatus(), productDetail, noProductDetail);
        Page<ClientPromotionResponse> responsePage = promotionPage.map(ClientPromotionMapper.INSTANCE::promotionToClientPromotionResponse);
        return new PageableObject<>(responsePage);

    }

    @Override
    public Object create(ClientPromotionRequest request) {
        return null;
    }

    @Override
    public ClientPromotionResponse update(ClientPromotionRequest request) {
        return null;
    }

    @Override
    public ClientPromotionResponse findById(String id) {
        Optional<Promotion> promotionOptional = clientPromotionRepository.findById(id);
        if (promotionOptional.isEmpty()) {
            throw new ResourceNotFoundException("PROMOTION IS NOT EXIST :" + id);
        }

        return ClientPromotionMapper.INSTANCE.promotionToClientPromotionResponse(promotionOptional.get());
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }

}
