package com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String creator;
    private Long at;

}
