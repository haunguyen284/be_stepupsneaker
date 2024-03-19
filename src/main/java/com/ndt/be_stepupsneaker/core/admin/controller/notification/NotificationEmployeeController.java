package com.ndt.be_stepupsneaker.core.admin.controller.notification;

import com.ndt.be_stepupsneaker.core.admin.dto.request.notification.NotificationEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.notification.NotificationEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.notification.NotificationOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminBrandResponse;
import com.ndt.be_stepupsneaker.core.admin.service.notification.NotificationEmployeeService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/admin/notifications")
public class NotificationEmployeeController {

    private final NotificationEmployeeService notificationEmployeeService;

    public NotificationEmployeeController(NotificationEmployeeService notificationEmployeeService) {
        this.notificationEmployeeService = notificationEmployeeService;
    }

    @GetMapping("")
    public Object findAllBrand(NotificationEmployeeRequest request){
        PageableObject<NotificationEmployeeResponse> listNotification = notificationEmployeeService.findAllEntity(request);
        return ResponseHelper.getResponse(listNotification, HttpStatus.OK);
    }

    @GetMapping("/sse")
    public Flux<ServerSentEvent<List<NotificationEmployeeResponse>>> steamNotification() {
        return notificationEmployeeService.findAllNotificationFlux();
    }

    @GetMapping("/sse/orders")
    public Flux<ServerSentEvent<List<NotificationOrderResponse>>> steamNotificationOrder() {
        return notificationEmployeeService.getOrderCountByStatusFlux();
    }

    @PutMapping("/read/{id}")
    public Object changeNotificationToRead(@PathVariable String id) {
        return ResponseHelper.getResponse(notificationEmployeeService.changeNotificationToRead(id), HttpStatus.OK);
    }

    @GetMapping("/read-all")
    public Object readAll() {
        return ResponseHelper.getResponse(notificationEmployeeService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/unread")
    public Object unRead(NotificationEmployeeRequest request) {
        return ResponseHelper.getResponse(notificationEmployeeService.unRead(request), HttpStatus.OK);
    }
}
