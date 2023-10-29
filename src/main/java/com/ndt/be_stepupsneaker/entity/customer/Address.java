package com.ndt.be_stepupsneaker.entity.customer;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    @Column(name = "city", length = EntityProperties.LENGTH_CITY)
    @Nationalized
    private String city;

    @Column(name = "province", length = EntityProperties.LENGTH_PROVINCE)
    @Nationalized
    private String province;

    @Column(name = "country", length = EntityProperties.LENGTH_COUNTRY)
    @Nationalized
    private String country;


}
