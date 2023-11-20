package com.ndt.be_stepupsneaker.repository.customer;

import com.ndt.be_stepupsneaker.entity.customer.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(AddressRepository.NAME)
public interface AddressRepository extends JpaRepository<Address, String> {
    public static final String NAME = "BaseAddressRepository";
}
