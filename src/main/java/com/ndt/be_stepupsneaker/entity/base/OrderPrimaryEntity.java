package com.ndt.be_stepupsneaker.entity.base;

import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.util.RandomStringUtil;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class OrderPrimaryEntity extends OrderAuditEntity{

    @Id
    @Column(name = "id", updatable = false, length = EntityProperties.LENGTH_ID)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code", updatable = false, length = EntityProperties.LENGTH_CODE, unique = true)
    private String code;


    @Column(name = "deleted", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean deleted = false;

    @PrePersist
    private void generateCode() {
        int codeLength = 5;
        String randomPart = RandomStringUtil.randomAlphaNumeric(codeLength);
        this.code = "SUS" + "-" + randomPart;
    }

}
