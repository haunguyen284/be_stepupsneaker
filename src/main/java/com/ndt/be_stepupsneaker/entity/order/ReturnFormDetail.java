package com.ndt.be_stepupsneaker.entity.order;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.ReturnInspectionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "return_form_detail")
@Entity
public class ReturnFormDetail extends PrimaryEntity {
    @JoinColumn(name = "order_detail_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private OrderDetail orderDetail;

    @JoinColumn(name = "return_form_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ReturnForm returnForm;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "reason", length = EntityProperties.LENGTH_NAME)
    private String reason;

    @Column(name = "feedback", length = EntityProperties.LENGTH_DESCRIPTION)
    private String feedback;

    @Column(name = "return_inspection_status")
    private ReturnInspectionStatus returnInspectionStatus;

    @Column(name = "return_inspection_reason", length = EntityProperties.LENGTH_DESCRIPTION)
    private String returnInspectionReason;

    @Column(name = "url_image")
    private String urlImage;

    @Column(name = "resellable")
    private boolean reSellable;
}
