package com.ndt.be_stepupsneaker.entity.envers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Getter
@Setter
@Entity
@Table(name = "audit_envers_info")
@RevisionEntity(UserRevisionListener.class)
public class AuditEnversInfo extends DefaultRevisionEntity {

    @Column(name = "username")
    private String username;
}
