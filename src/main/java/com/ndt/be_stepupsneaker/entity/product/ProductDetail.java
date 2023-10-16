package com.ndt.be_stepupsneaker.entity.product;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
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
@Table(name = "product_detail")
@Entity
public class ProductDetail extends PrimaryEntity {

    @JoinColumn(name = "trade_mark_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TradeMark tradeMark;

    @JoinColumn(name = "style_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Style style;

    @JoinColumn(name = "size_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Size size;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Material material;

    @JoinColumn(name = "color_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Color color;

    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @JoinColumn(name = "sole_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Sole sole;

    @Column(name = "url_image")
    private String image;

    @Column(name = "price")
    private float price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "status")
    private ProductStatus status;
}

