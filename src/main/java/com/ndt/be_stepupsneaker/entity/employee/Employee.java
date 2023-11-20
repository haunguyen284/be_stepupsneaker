package com.ndt.be_stepupsneaker.entity.employee;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EmployeeStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Table(name = "employee")
@Entity
public class Employee extends PrimaryEntity {
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @Column(name = "full_name", length = EntityProperties.LENGTH_NAME, nullable = false, unique = true)
    @Nationalized
    private String fullName;

    @Column(name = "email", length = EntityProperties.LENGTH_EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = EntityProperties.LENGTH_PASSWORD, nullable = false)
    private String password;

    @Column(name = "status")
    private EmployeeStatus status;

    @Column(name = "address", length = EntityProperties.LENGTH_ADDRESS)
    @Nationalized
    private String address;

    @Column(name = "gender", length = EntityProperties.LENGTH_GENDER)
    @Nationalized
    private String gender;

    @Column(name = "phone_number", length = EntityProperties.LENGTH_PHONE)
    private String phoneNumber;

    @Column(name = "url_image")
    private String image;

}
