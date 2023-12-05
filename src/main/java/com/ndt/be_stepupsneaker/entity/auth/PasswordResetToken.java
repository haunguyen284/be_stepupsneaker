package com.ndt.be_stepupsneaker.entity.auth;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_reset_token")
public class PasswordResetToken extends PrimaryEntity {

    @Column(name = "token")
    private String token;

    @Column(name = "exporation_time")
    private Date expirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
