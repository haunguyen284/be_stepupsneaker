package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "order_detail")
@Entity
public class OrderDetail extends PrimaryEntity {
    @JoinColumn(name = "product_detail_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDetail productDetail;

    @JoinColumn(name = "shop_order_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private float price;

    @Column(name = "total_price")
    private float totalPrice;

    @Column(name = "status")
    private OrderStatus status;

}
