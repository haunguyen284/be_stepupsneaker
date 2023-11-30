package com.ndt.be_stepupsneaker.core.admin.dto.response.notification;

import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.NotificationEmployeeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationEmployeeResponse {
    private String id;

    private String content;

    private Employee employee;

    private Customer customer;

    private String href;

    private NotificationEmployeeType notificationType;

    private boolean read;

    private boolean delivered;
}
