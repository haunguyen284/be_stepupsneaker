package com.ndt.be_stepupsneaker.entity.customer;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.product.Sole;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Table(name = "address")
@Entity
public class Address extends PrimaryEntity {
    @Column(name = "phone_number", length = EntityProperties.LENGTH_PHONE)
    private String phoneNumber;

    @Column(name = "is_default", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDefault;

    @Column(name = "district", length = EntityProperties.LENGTH_CITY)
    @Nationalized
    private String district;

    @Column(name = "province", length = EntityProperties.LENGTH_PROVINCE)
    @Nationalized
    private String province;

    @Column(name = "ward", length = EntityProperties.LENGTH_COUNTRY)
    @Nationalized
    private String ward;

    @Column(name = "more", length = EntityProperties.LENGTH_COUNTRY)
    @Nationalized
    private String more;

    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

}
