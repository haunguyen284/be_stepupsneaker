package com.ndt.be_stepupsneaker.entity.employee;

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
@Table(name = "role")
@Entity
public class Role extends PrimaryEntity {
    @Column(name = "name", length = EntityProperties.LENGTH_NAME, nullable = false, unique = true)
    @Nationalized
    private String name;
}
