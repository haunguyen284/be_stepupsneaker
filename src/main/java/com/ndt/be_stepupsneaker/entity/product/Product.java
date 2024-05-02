package com.ndt.be_stepupsneaker.entity.product;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.Set;

@Getter
@Setter
@Table(name = "product")
@Entity
public class Product extends PrimaryEntity {
    @Column(name = "code", length = EntityProperties.LENGTH_CODE, nullable = false)
    private String code;

    @Column(name = "name", length = EntityProperties.LENGTH_NAME, nullable = false)
    @Nationalized
    private String name;

    @Column(name = "description", length = EntityProperties.LENGTH_DESCRIPTION)
    @Nationalized
    private String description;

    @Column(name = "url_image")
    private String image;

    @Column(name = "status")
    private ProductStatus status;

    @OneToMany(mappedBy="product")
    private Set<ProductDetail> productDetails;
}

