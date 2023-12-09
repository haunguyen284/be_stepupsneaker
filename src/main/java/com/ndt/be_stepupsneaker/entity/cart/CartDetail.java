package com.ndt.be_stepupsneaker.entity.cart;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.product.ProductDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "cart_detail")
@Entity
public class CartDetail extends PrimaryEntity{
    @JoinColumn(name = "product_detail_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductDetail productDetail;

    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @Column(name = "quantity")
    private int quantity;

}
