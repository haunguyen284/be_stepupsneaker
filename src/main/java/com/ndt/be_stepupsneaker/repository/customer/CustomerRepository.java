package com.ndt.be_stepupsneaker.repository.customer;

import com.ndt.be_stepupsneaker.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(CustomerRepository.NAME)
public interface CustomerRepository extends JpaRepository<Customer , String> {
    public static final String NAME = "BaseCustomerRepository";

}
