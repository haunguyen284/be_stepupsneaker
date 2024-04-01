package com.ndt.be_stepupsneaker.core.client.dto.response.review;

import com.ndt.be_stepupsneaker.entity.order.Order;

public interface OrderWithReviewCountResponse {
    Order getOrder();
    Long getCountReview();
}
