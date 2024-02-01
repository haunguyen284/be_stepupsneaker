package com.ndt.be_stepupsneaker.core.admin.service.impl.order_audit;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit.AdminOrderAuditResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order_audit.ChangeDetailResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.order.AdminOrderMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderRepository;
import com.ndt.be_stepupsneaker.core.admin.service.order_audit.AdminOrderAuditService;
import com.ndt.be_stepupsneaker.entity.envers.AuditEnversInfo;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.util.EntityComparator;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.NotAudited;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminOrderAuditServiceImpl implements AdminOrderAuditService {

    private final AdminOrderRepository adminOrderRepository;

    @Override
    public List<AdminOrderAuditResponse> getOrderRevisions(String id) {
        List<Revision<Integer, Order>> revisions = adminOrderRepository.findRevisions(id).getContent();
        List<AdminOrderAuditResponse> auditResponses = new ArrayList<>();

        if (revisions.isEmpty()) {
            return auditResponses;
        }

        for (int i = 0; i < revisions.size(); i++) {
            Revision<Integer, Order> currentRevision = revisions.get(i);
            RevisionMetadata<Integer> metadata = currentRevision.getMetadata();
            AuditEnversInfo auditEnversInfo = metadata.getDelegate();

            AdminOrderResponse currentOrder = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(currentRevision.getEntity());

            AdminOrderAuditResponse auditResponse = new AdminOrderAuditResponse();
            auditResponse.setRevisionNumber(i + 1);
            auditResponse.setRevisionType(metadata.getRevisionType());
            auditResponse.setEntity(currentOrder);
            auditResponse.setCreator(auditEnversInfo.getUsername());
            auditResponse.setAt(auditEnversInfo.getTimestamp());

            if (i > 0) {
                Revision<Integer, Order> previousRevision = revisions.get(i - 1);
                AdminOrderResponse previousOrder = AdminOrderMapper.INSTANCE.orderToAdminOrderResponse(previousRevision.getEntity());

                if (metadata.getRevisionType() == RevisionMetadata.RevisionType.UPDATE) {
                    auditResponse.setChanges(findChanges(currentOrder, previousOrder));
                }
            }

            auditResponses.add(auditResponse);
        }

        return auditResponses;
    }


    public Map<String, ChangeDetailResponse<?>> findChanges(AdminOrderResponse newResponse, AdminOrderResponse oldResponse) {
        Map<String, ChangeDetailResponse<?>> changes = new HashMap<>();

        Field[] fields = AdminOrderResponse.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(NotAudited.class)) {
                field.setAccessible(true);
                try {
                    Object oldValue = field.get(oldResponse);
                    Object newValue = field.get(newResponse);

                    if (oldValue != null && newValue != null) {
                        if (EntityComparator.isBaseType(oldValue.getClass()) && EntityComparator.isBaseType(newValue.getClass())) {
                            if (!Objects.equals(oldValue, newValue)) {
                                ChangeDetailResponse<Object> changeDetail = new ChangeDetailResponse<>();
                                changeDetail.setOldValue(oldValue);
                                changeDetail.setNewValue(newValue);
                                changes.put(field.getName(), changeDetail);
                            }
                        } else {
                            Javers javers = JaversBuilder.javers().build();
                            Diff diff = null;

                            if (oldValue instanceof List && newValue instanceof List) {
                                @SuppressWarnings("unchecked")
                                List<AdminOrderDetailResponse> oldList = (List<AdminOrderDetailResponse>) oldValue;
                                @SuppressWarnings("unchecked")
                                List<AdminOrderDetailResponse> newList = (List<AdminOrderDetailResponse>) newValue;

                                diff = javers.compareCollections(oldList, newList, AdminOrderDetailResponse.class);
                            } else {
                                diff = javers.compare(oldValue, newValue);
                            }

                            if (diff.hasChanges()) {
                                ChangeDetailResponse<Object> changeDetail = new ChangeDetailResponse<>();
                                changeDetail.setOldValue(oldValue);
                                changeDetail.setNewValue(newValue);
                                changes.put(field.getName(), changeDetail);
                            }
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
