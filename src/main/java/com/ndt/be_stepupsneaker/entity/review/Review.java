package com.ndt.be_stepupsneaker.entity.review;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReviewStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends PrimaryEntity {

    @JoinColumn(name = "product_detail_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDetail productDetail;


    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "comment", length = EntityProperties.LENGTH_DESCRIPTION)
    private String comment;

    @Column(name = "rating")
    private int rating;

    @Column(name = "url_image")
    private String urlImage;

    @Column(name = "status")
    private ReviewStatus status;
}
