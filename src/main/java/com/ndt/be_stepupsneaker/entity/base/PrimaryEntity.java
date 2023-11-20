package com.ndt.be_stepupsneaker.entity.base;

import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.listener.CreatePrimaryEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(CreatePrimaryEntityListener.class)
public abstract class PrimaryEntity extends AuditEntity implements IsIdentified {

    @Id
    @Column(name = "id", updatable = false, length = EntityProperties.LENGTH_ID)
    private String id;

    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted = false;

}
