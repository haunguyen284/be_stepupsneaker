package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Table(name = "order_history")
@Entity
public class OrderHistory extends PrimaryEntity {

    @JoinColumn(name = "shop_order_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(name = "action_description", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String actionDescription;

    @Column(name = "action_status", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private OrderStatus actionStatus;

    @Column(name = "note", length = EntityProperties.LENGTH_NAME)
    @Nationalized
    private String note;

}
