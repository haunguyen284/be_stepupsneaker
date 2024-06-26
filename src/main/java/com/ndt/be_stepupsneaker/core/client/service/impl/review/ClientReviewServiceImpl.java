package com.ndt.be_stepupsneaker.core.client.service.impl.review;

import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderDetailMapper;
import com.ndt.be_stepupsneaker.core.client.dto.request.review.ClientReviewRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.ClientReviewResponse;
import com.ndt.be_stepupsneaker.core.client.mapper.review.ClientReviewMapper;
import com.ndt.be_stepupsneaker.core.client.repository.customer.ClientCustomerRepository;
import com.ndt.be_stepupsneaker.core.client.repository.order.ClientOrderRepository;
import com.ndt.be_stepupsneaker.core.client.repository.product.ClientProductDetailRepository;
import com.ndt.be_stepupsneaker.core.client.repository.review.ClientReviewRepository;
import com.ndt.be_stepupsneaker.core.client.service.review.ClientReviewService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.entity.review.Review;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReviewStatus;
import com.ndt.be_stepupsneaker.infrastructure.exception.ResourceNotFoundException;
import com.ndt.be_stepupsneaker.infrastructure.security.session.MySessionInfo;
import com.ndt.be_stepupsneaker.util.CloudinaryUpload;
import com.ndt.be_stepupsneaker.util.MessageUtil;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientReviewServiceImpl implements ClientReviewService {

    private final ClientReviewRepository clientReviewRepository;
    private final ClientCustomerRepository clientCustomerRepository;
    private final ClientProductDetailRepository clientProductDetailRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final CloudinaryUpload cloudinaryUpload;
    private final MySessionInfo mySessionInfo;
    private final PaginationUtil paginationUtil;
    private final MessageUtil messageUtil;

    @Override
    public PageableObject<ClientReviewResponse> findAllEntity(ClientReviewRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<Review> reviews = clientReviewRepository.findAllReview(request, pageable);
        if (getSession() != null) {
            Page<Review> reviewsByCustomer = clientReviewRepository.findAllReviewByCustomerAndStatus(request, pageable, getSession().getId());
            List<Review> allReview = new ArrayList<>(reviews.getContent());
            allReview.addAll(reviewsByCustomer.getContent());
            Page<Review> combinedPage = new PageImpl<>(allReview, pageable, allReview.size());
            Page<ClientReviewResponse> reviewResponses = combinedPage.map(ClientReviewMapper.INSTANCE::reviewToClientReviewResponse);
            return new PageableObject<>(reviewResponses);
        } else {
            Page<ClientReviewResponse> adminCustomerVoucherResponsePage = reviews.map(ClientReviewMapper.INSTANCE::reviewToClientReviewResponse);
            return new PageableObject<>(adminCustomerVoucherResponsePage);
        }
    }

    @Override
    public Object create(ClientReviewRequest request) {
        throw new ResourceNotFoundException("API not support");
    }

    @Override
    public ClientReviewResponse update(ClientReviewRequest request) {
        Customer customer = getCustomer();
        Review review = clientReviewRepository.findByCustomerAndId(customer, request.getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("review_notfound")));
        if (review.getStatus() == ReviewStatus.REJECTED) {
            throw new ResourceNotFoundException(messageUtil.getMessage("review.cant_update"));
        }
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        if (request.getUrlImage() != null) {
            request.setUrlImage(cloudinaryUpload.upload(request.getUrlImage()));
            review.setUrlImage(request.getUrlImage());
        }
        return ClientReviewMapper.INSTANCE.reviewToClientReviewResponse(clientReviewRepository.save(review));
    }

    @Override
    public ClientReviewResponse findById(String id) {
        Customer customer = getCustomer();
        Review review = clientReviewRepository.findByCustomerAndId(customer, id)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("review_notfound")));
        return ClientReviewMapper.INSTANCE.reviewToClientReviewResponse(review);
    }

    @Override
    public Boolean delete(String id) {
        Customer customer = getCustomer();
        Review review = clientReviewRepository.findByCustomerAndId(customer, id)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("review_notfound")));
        review.setDeleted(true);
        clientReviewRepository.save(review);
        return true;
    }


    @Override
    public List<ClientReviewResponse> create(List<ClientReviewRequest> reviewRequests) {
        List<Review> reviews = new ArrayList<>();
        for (ClientReviewRequest request : reviewRequests) {
            Customer customer = getCustomer();
            ProductDetail productDetail = clientProductDetailRepository.findById(request.getProductDetail())
                    .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("product.product_detail.notfound")));
            List<Order> orders = customer.getOrders();
            Order constOrder = null;
            if (request.getOrder() == null || request.getOrder().equals("")) {
                boolean isCheck = false;
                for (Order order : orders) {
                    List<OrderDetail> orderDetails = order.getOrderDetails();
                    for (OrderDetail orderDetail : orderDetails) {
                        if (orderDetail.getProductDetail() == productDetail) {
                            isCheck = true;
                            constOrder = order;
                            break;
                        }
                    }
                    if (isCheck) {
                        break;
                    }
                }
                if (isCheck) {
                    if (constOrder.getStatus() != OrderStatus.COMPLETED) {
                        throw new ResourceNotFoundException(messageUtil.getMessage("review.status.not.completed"));

                    }
                } else {
                    throw new ResourceNotFoundException(messageUtil.getMessage("review_message"));

                }

                constOrder = null;
            } else {
                constOrder = clientOrderRepository.findById(request.getOrder())
                        .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("order.notfound")));
                if (constOrder.getStatus() != OrderStatus.COMPLETED) {
                    throw new ResourceNotFoundException(messageUtil.getMessage("review.status.not.completed"));
                }
            }
            if (request.getUrlImage() != null) {
                request.setUrlImage(cloudinaryUpload.upload(request.getUrlImage()));
            }
            Review review = ClientReviewMapper.INSTANCE.clientReviewRequestToReview(request);
            review.setOrder(constOrder);
            review.setCustomer(customer);
            review.setProductDetail(productDetail);
            review.setStatus(ReviewStatus.WAITING);
            reviews.add(review);
        }
        return clientReviewRepository.saveAll(reviews)
                .stream()
                .map(ClientReviewMapper.INSTANCE::reviewToClientReviewResponse)
                .collect(Collectors.toList());
    }

    private Customer getCustomer() {
        ClientCustomerResponse customerResponse = mySessionInfo.getCurrentCustomer();
        if (customerResponse == null) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.not_login"));
        }
        Customer customer = clientCustomerRepository.findById(customerResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("customer.notfound")));
        return customer;
    }

    private Customer getSession() {
        ClientCustomerResponse customerResponse = mySessionInfo.getCurrentCustomer();
        if (customerResponse == null) {
            return null;
        }
        Customer customer = clientCustomerRepository.findById(customerResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("customer.notfound")));
        return customer;
    }
}
