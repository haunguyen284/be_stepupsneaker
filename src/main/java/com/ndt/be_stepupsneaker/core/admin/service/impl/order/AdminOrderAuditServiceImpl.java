package com.ndt.be_stepupsneaker.core.admin.service.impl.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit.AdminOrderAuditResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit.ChangeDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderAuditService;
import com.ndt.be_stepupsneaker.entity.order.Order;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.NotAudited;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminOrderAuditServiceImpl implements AdminOrderAuditService {

    private final AdminOrderRepository adminOrderRepository;

    @Override
    public List<AdminOrderAuditResponse> getOrderRevisions(String id) {
        List<Revision<Integer, Order>> revisions = adminOrderRepository.findRevisions(id).stream().toList();
        List<AdminOrderAuditResponse> auditResponses = new ArrayList<>();

        for (int i = 1; i < revisions.size(); i++) {
            Revision<Integer, Order> currentRevision = revisions.get(i);
            Order currentOrder = currentRevision.getEntity();
            AdminOrderResponse currentAdminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(currentOrder);

            AdminOrderAuditResponse currentAuditResponse = new AdminOrderAuditResponse();
            RevisionMetadata<Integer> currentMetadata = currentRevision.getMetadata();
            currentAuditResponse.setRevisionNumber(currentMetadata.getRevisionNumber().orElseThrow());
            currentAuditResponse.setRevisionType(currentMetadata.getRevisionType());
            currentAuditResponse.setEntity(currentAdminOrderResponse);

            AdminOrderResponse previousAdminOrderResponse = null;
            if (i > 0) {
                Revision<Integer, Order> previousRevision = revisions.get(i - 1);
                Order previousOrder = previousRevision.getEntity();
                previousAdminOrderResponse = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(previousOrder);
            }

            currentAuditResponse.setChanges(findChanges(currentAdminOrderResponse,previousAdminOrderResponse));
            auditResponses.add(currentAuditResponse);
        }

        return auditResponses;
    }

    public Map<String, ChangeDetailResponse<?>> findChanges(AdminOrderResponse newResponse,AdminOrderResponse oldResponse) {
        Map<String, ChangeDetailResponse<?>> changes = new HashMap<>();

        Field[] fields = AdminOrderResponse.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(NotAudited.class)) {
                field.setAccessible(true);
                try {
                    Object oldValue = field.get(oldResponse);
                    Object newValue = field.get(newResponse);

                    if (!Objects.equals(oldValue, newValue)) {
                        if (oldValue != null && newValue != null) {
                            ChangeDetailResponse<Object> changeDetail = new ChangeDetailResponse<>();
                            changeDetail.setOldValue(oldValue);
                            changeDetail.setNewValue(newValue);
                            changes.put(field.getName(), changeDetail);
                        }
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return changes;
    }


}
