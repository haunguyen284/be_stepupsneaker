package com.ndt.be_stepupsneaker.repository.auth;

import com.ndt.be_stepupsneaker.entity.auth.PasswordResetToken;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByCustomerAndEmployee(Customer customer, Employee employee);

}
