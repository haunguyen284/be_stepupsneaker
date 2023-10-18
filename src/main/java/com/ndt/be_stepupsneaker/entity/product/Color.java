package com.ndt.be_stepupsneaker.entity.product;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Table(name = "color")
@Entity
public class Color extends PrimaryEntity {
    @Column(name = "code", length = EntityProperties.LENGTH_CODE, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = EntityProperties.LENGTH_NAME, nullable = false, unique = true)
    @Nationalized
    private String name;

    @Column(name = "status")
    private ProductPropertiesStatus status;
}

