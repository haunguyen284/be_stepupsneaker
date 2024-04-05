package com.ndt.be_stepupsneaker.core.admin.service.impl.review;

import com.ndt.be_stepupsneaker.core.admin.dto.request.review.AdminReviewRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.review.AdminReviewResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.review.AdminReviewMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.customer.AdminCustomerRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.product.AdminProductDetailRepository;
import com.ndt.be_stepupsneaker.core.admin.repository.review.AdminReviewRepository;
import com.ndt.be_stepupsneaker.core.admin.service.review.AdminReviewService;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.review.ClientReviewMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.review.ClientReviewRepository;
import com.ndt.be_stepupsneaker.core.client.service.review.ClientReviewService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.review.Review;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReviewStatus;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;
    private final AdminCustomerRepository adminCustomerRepository;
    private final AdminProductDetailRepository adminProductDetailRepository;
    private final CloudinaryUpload cloudinaryUpload;
    private final MySessionInfo mySessionInfo;
    private final PaginationUtil paginationUtil;
    private final MessageUtil messageUtil;

    @Override
    public PageableObject<AdminReviewResponse> findAllEntity(AdminReviewRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<Review> resp = adminReviewRepository.findAllReview(request, pageable);
        Page<AdminReviewResponse> clientOrderResponses = resp.map(AdminReviewMapper.INSTANCE::reviewToAdminReviewResponse);
        return new PageableObject<>(clientOrderResponses);
    }

    @Override
    public Object create(AdminReviewRequest request) {
       return null;
    }

    @Override
    public AdminReviewResponse update(AdminReviewRequest request) {
        Review review = adminReviewRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("review_notfound")));
        if (review.getStatus() == ReviewStatus.REJECTED) {
            throw new ResourceNotFoundException(messageUtil.getMessage("review.cant_update"));
        }
        review.setStatus(request.getStatus());
        return AdminReviewMapper.INSTANCE.reviewToAdminReviewResponse(adminReviewRepository.save(review));
    }

    @Override
    public AdminReviewResponse findById(String id) {
        Review review = adminReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("review_notfound")));
        return AdminReviewMapper.INSTANCE.reviewToAdminReviewResponse(review);
    }

    @Override
    public Boolean delete(String id) {
        Review review = adminReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("review_notfound")));
        review.setDeleted(true);
        adminReviewRepository.save(review);
        return true;
    }
}
