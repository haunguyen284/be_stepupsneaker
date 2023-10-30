package com.ndt.be_stepupsneaker.core.admin.repository.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.repository.customer.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminAddressRepository extends AddressRepository {
    @Query("""
            SELECT x FROM Address x 
            WHERE
            (:#{#request.q} IS NULL OR :#{#request.q} LIKE '' 
            OR x.phoneNumber LIKE CONCAT('%', :#{#request.q}, '%')  
            OR x.more LIKE CONCAT('%', :#{#request.q}, '%')
            OR x.province LIKE CONCAT('%', :#{#request.q}, '%')
            OR x.district LIKE CONCAT('%', :#{#request.q}, '%')
            OR x.ward LIKE CONCAT('%', :#{#request.q}, '%'))
            AND(x.deleted=FALSE)
            """)
    Page<Address> findAllAddress(@Param("request") AdminAddressRequest request, Pageable pageable);

    Optional<Address> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT x FROM Address x WHERE (x.phoneNumber = :phoneNumber AND :phoneNumber IN ('SELECT y.phoneNumber FROM Address y WHERE y.id != :id'))
            """)
    Optional<Address> findByPhoneNumber(@Param("id") UUID id, @Param("phoneNumber") String phoneNumber);

    Address findByIsDefault(Boolean isDefault);

    @Query("""
            SELECT x FROM Address x 
            WHERE x.customer.id  = :customerId 
            AND
            (:#{#request.q} IS NULL OR :#{#request.q} LIKE '' 
            OR x.ward LIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.province LIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.district LIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.more LIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.phoneNumber LIKE  CONCAT('%', :#{#request.q}, '%'))
            AND
            (x.deleted = FALSE)
             """)
    Page<Address>findAllAddressByCustomerId(@Param("customerId") UUID customerId, @Param("request") AdminAddressRequest request,Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN TRUE ELSE FALSE END FROM Address x WHERE x.customer = :customer")
    boolean existsByCustomer(@Param("customer") Customer customer);

    @Query("SELECT x FROM Address x WHERE x.customer.id = :customerId AND x.isDefault = TRUE")
    Address findDefaultAddressByCustomer(@Param("customerId") UUID customerId);

    List<Address> findByCustomer(Customer customer);
}
