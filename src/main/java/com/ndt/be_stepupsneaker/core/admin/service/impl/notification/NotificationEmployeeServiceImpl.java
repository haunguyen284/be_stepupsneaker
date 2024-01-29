package com.ndt.be_stepupsneaker.core.admin.service.impl.notification;

import com.ndt.be_stepupsneaker.core.admin.dto.request.notification.NotificationEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.notification.NotificationEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.notification.NotificationOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.customer.AdminAddressMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.notification.NotificationEmployeeMapper;
import com.ndt.be_stepupsneaker.core.admin.mapper.product.AdminBrandMapper;
import com.ndt.be_stepupsneaker.core.admin.repository.notification.AdminNotificationEmployeeRepository;
import com.ndt.be_stepupsneaker.core.admin.service.notification.NotificationEmployeeService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.notification.NotificationEmployee;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.infrastructure.exception.ApiException;
import com.ndt.be_stepupsneaker.repository.notification.NotificationEmployeeRepository;
import com.ndt.be_stepupsneaker.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationEmployeeServiceImpl implements NotificationEmployeeService {

    @Autowired
    private AdminNotificationEmployeeRepository notificationEmployeeRepository;

    @Autowired
    private PaginationUtil paginationUtil;

    private List<NotificationEmployeeResponse> findAll(){
        List<NotificationEmployee> notificationEmployees = notificationEmployeeRepository.findAllByDeliveredFalse();
        notificationEmployees.forEach(x -> x.setDelivered(true));
        notificationEmployeeRepository.saveAll(notificationEmployees);
        return notificationEmployees.stream().map(NotificationEmployeeMapper.INSTANCE::notificationEmployeeToNotificationEmployeeResponse).toList();
    }

    private List<NotificationOrderResponse> getOrderCount() {
        List<NotificationOrderResponse> notificationOrderResponses = new ArrayList<>();
        Object[] results = notificationEmployeeRepository.getOrderCountByStatus();
        for (Object result : results) {
            Object[] resultArray = (Object[]) result;
            OrderStatus status = (OrderStatus) resultArray[0];
            Long count = (Long) resultArray[1];
            NotificationOrderResponse response = new NotificationOrderResponse(status, count.intValue());
            notificationOrderResponses.add(response);
        }
        return notificationOrderResponses;
    }

    @Override
    public Flux<ServerSentEvent<List<NotificationEmployeeResponse>>> findAllNotificationFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> ServerSentEvent.<List<NotificationEmployeeResponse>>builder().data(findAll())
                        .build());
    }

    @Override
    public PageableObject<NotificationEmployeeResponse> findAllEntity(NotificationEmployeeRequest request) {
        Pageable pageable = paginationUtil.pageable(request);
        Page<NotificationEmployee> resp = notificationEmployeeRepository.findAll(pageable);
        Page<NotificationEmployeeResponse> notificationEmployeeResponses = resp.map(NotificationEmployeeMapper.INSTANCE::notificationEmployeeToNotificationEmployeeResponse);
        return new PageableObject<>(notificationEmployeeResponses);
    }

    @Override
    public NotificationEmployeeResponse changeNotificationToRead(String id) {
        NotificationEmployee notificationEmployee = notificationEmployeeRepository.findById(id).orElseThrow(() -> new ApiException("Notification not found"));
        notificationEmployee.setRead(true);

        return NotificationEmployeeMapper.INSTANCE.notificationEmployeeToNotificationEmployeeResponse(notificationEmployeeRepository.save(notificationEmployee));
    }

    @Override
    public Flux<ServerSentEvent<List<NotificationOrderResponse>>> getOrderCountByStatusFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> ServerSentEvent.<List<NotificationOrderResponse>>builder().data(getOrderCount())
                        .build());
    }

}
