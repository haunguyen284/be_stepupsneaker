package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnFormStatus;
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
@Table(name = "return_form_history")
@Entity
public class ReturnFormHistory extends PrimaryEntity {
    @JoinColumn(name = "return_form_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReturnForm returnForm;

    @Column(name = "action_status")
    private ReturnFormStatus actionStatus;

    @Column(name = "note", length = EntityProperties.LENGTH_DESCRIPTION)
    @Nationalized
    private String note;
}
