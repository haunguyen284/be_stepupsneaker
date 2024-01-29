package com.ndt.be_stepupsneaker.core.admin.service.notification;

import com.ndt.be_stepupsneaker.core.admin.dto.request.notification.NotificationEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.notification.NotificationEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.notification.NotificationOrderResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;

public interface NotificationEmployeeService {
    Flux<ServerSentEvent<List<NotificationEmployeeResponse>>> findAllNotificationFlux();

    PageableObject<NotificationEmployeeResponse> findAllEntity(NotificationEmployeeRequest request);

    NotificationEmployeeResponse changeNotificationToRead(String id);

    Flux<ServerSentEvent<List<NotificationOrderResponse>>> getOrderCountByStatusFlux();

}
