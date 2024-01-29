package com.ndt.be_stepupsneaker.entity.notification;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import com.ndt.be_stepupsneaker.infrastructure.constant.NotificationEmployeeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Table(name = "notification_employee")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmployee extends PrimaryEntity {
    @Column(name = "content", length = EntityProperties.LENGTH_DESCRIPTION)
    private String content;

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;

    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    @Column(name = "href", length = EntityProperties.LENGTH_HREF)
    private String href;

    @Column(name = "type")
    private NotificationEmployeeType notificationType;

    @Column(name = "read", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean read;

    @Column(name = "delivered", columnDefinition = "BOOLEAN DEFAULT false")
    private boolean delivered;

}
