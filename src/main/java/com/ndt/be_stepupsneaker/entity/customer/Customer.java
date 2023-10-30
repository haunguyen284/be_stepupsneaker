package com.ndt.be_stepupsneaker.entity.customer;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.List;

@Getter
@Setter
@Table(name = "customer")
@Entity

public class Customer extends PrimaryEntity {
    @Column(name = "full_name", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String fullName;

    @Column(name = "email", length = EntityProperties.LENGTH_EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = "date_of_birth")
    private Long dateOfBirth;

    @Column(name = "password", length = EntityProperties.LENGTH_PASSWORD, nullable = false)
    private String password;

    @Column(name = "status")
    private CustomerStatus status;

    @Column(name = "gender", length = EntityProperties.LENGTH_GENDER)
    @Nationalized
    private String gender;

    @Column(name = "url_image")
    @Lob
    private String image;



}

