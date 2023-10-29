package com.ndt.be_stepupsneaker.entity.base;

import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class PrimaryEntity extends AuditEntity{

    @Id
    @Column(name = "id", updatable = false, length = EntityProperties.LENGTH_ID)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted = false;

}
