package com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionType;
import org.springframework.data.history.RevisionMetadata;

import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminOrderAuditResponse {
    private RevisionMetadata.RevisionType revisionType;
    private Integer revisionNumber;
    private AdminOrderResponse entity;
    private Map<String, ChangeDetailResponse<?>> changes;

}
