package com.ndt.be_stepupsneaker.entity.base;

import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.listener.AuditEntityListener;
import com.ndt.be_stepupsneaker.infrastructure.listener.OrderAuditEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(OrderAuditEntityListener.class)
public abstract class OrderAuditEntity extends AuditEntity{

    @Transient
    private transient Order previousOrderState;

}