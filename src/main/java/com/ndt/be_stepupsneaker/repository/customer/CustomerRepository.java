package com.ndt.be_stepupsneaker.repository.customer;

import com.ndt.be_stepupsneaker.entity.customer.Customer;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(CustomerRepository.NAME)
public interface CustomerRepository extends JpaRepository<Customer , UUID> {
    public static final String NAME = "BaseCustomerRepository";

}
