package com.ndt.be_stepupsneaker.core.admin.repository.notification;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminProductRequest;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductStatus;
import com.ndt.be_stepupsneaker.repository.notification.NotificationEmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNotificationEmployeeRepository extends NotificationEmployeeRepository {
    @Query("""
    SELECT x.order.status, COUNT(DISTINCT x.order.code) FROM NotificationEmployee x WHERE x.read = false GROUP BY x.order.status
    """)
    Object[] getOrderCountByStatus();
}
