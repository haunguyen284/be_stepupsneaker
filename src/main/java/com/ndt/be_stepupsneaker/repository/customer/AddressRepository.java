package com.ndt.be_stepupsneaker.repository.customer;

import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository(AddressRepository.NAME)
public interface AddressRepository extends JpaRepository<Address, String> {
    public static final String NAME = "BaseAddressRepository";

    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN TRUE ELSE FALSE END FROM Address x WHERE x.customer = :customer AND x.deleted = FALSE AND x.isDefault =TRUE ")
    boolean existsByCustomer(@Param("customer") Customer customer);

    @Query("SELECT x FROM Address x WHERE x.customer.id = :customerId AND x.isDefault = TRUE AND x.deleted = FALSE ")
    Address findDefaultAddressByCustomer(@Param("customerId") String customerId);


    List<Address> findByCustomerAndDeleted(Customer customer, Boolean deleted);


    Optional<Address> findByIdAndCustomer(String id, Customer customer);
}
