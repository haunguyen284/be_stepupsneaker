package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "return_form")
@Entity
public class ReturnForm extends PrimaryEntity {

    @JoinColumn(name = "shop_order_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(name = "reason", length = EntityProperties.LENGTH_NAME)
    private String reason;

    @Column(name = "feedback", length = EntityProperties.LENGTH_DESCRIPTION)
    private String feedback;

    @Column(name = "status")
    private ReturnFormStatus status;

}
