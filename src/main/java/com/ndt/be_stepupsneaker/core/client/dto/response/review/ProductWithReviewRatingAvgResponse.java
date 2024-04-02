package com.ndt.be_stepupsneaker.core.client.dto.response.review;

import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.product.Product;

public interface ProductWithReviewRatingAvgResponse {
    Product getProduct();
    Double getAverageRating();
}
