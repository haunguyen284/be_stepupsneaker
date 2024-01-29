package com.ndt.be_stepupsneaker.repository.notification;

import com.ndt.be_stepupsneaker.core.admin.dto.request.notification.NotificationEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.entity.notification.NotificationEmployee;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(NotificationEmployeeRepository.NAME)
@Primary
public interface NotificationEmployeeRepository extends JpaRepository<NotificationEmployee, String> {
    public static final String NAME = "BaseNotificationEmployeeRepository";

    @Query("""
    SELECT x FROM NotificationEmployee x WHERE x.delivered=true
    """)
    Page<NotificationEmployee> findAllNotification(Pageable pageable);


    List<NotificationEmployee> findAllByDeliveredFalse();
}