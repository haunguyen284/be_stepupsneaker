package com.ndt.be_stepupsneaker.core.admin.mapper.notification;

import com.ndt.be_stepupsneaker.core.admin.dto.request.employee.AdminEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.notification.NotificationEmployeeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.employee.AdminEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.notification.NotificationEmployeeResponse;
import com.ndt.be_stepupsneaker.core.admin.mapper.empolyee.AdminEmployeeMapper;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.notification.NotificationEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationEmployeeMapper {
    NotificationEmployeeMapper INSTANCE = Mappers.getMapper(NotificationEmployeeMapper.class);

    NotificationEmployeeResponse notificationEmployeeToNotificationEmployeeResponse(NotificationEmployee notificationEmployee);

    NotificationEmployee notificationEmployeeRequestToNotificationEmployee(NotificationEmployeeRequest notificationEmployeeRequest);
}

