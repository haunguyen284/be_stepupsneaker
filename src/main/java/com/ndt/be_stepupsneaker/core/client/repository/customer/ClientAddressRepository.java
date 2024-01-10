package com.ndt.be_stepupsneaker.core.client.repository.customer;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientAddressRequest;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.repository.customer.AddressRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientAddressRepository extends AddressRepository {
    Optional<Address> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT x FROM Address x WHERE (x.phoneNumber = :phoneNumber AND :phoneNumber IN ('SELECT y.phoneNumber FROM Address y WHERE y.id != :id'))
            """)
    Optional<Address> findByPhoneNumber(@Param("id") String id, @Param("phoneNumber") String phoneNumber);

    Address findByIsDefault(Boolean isDefault);

    @Query("""
            SELECT x FROM Address x 
            WHERE (:customerId IS NULL OR x.customer.id  = :customerId)
            AND
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' 
            OR x.wardName ILIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.provinceName ILIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.districtName ILIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.more ILIKE  CONCAT('%', :#{#request.q}, '%')
            OR x.phoneNumber ILIKE  CONCAT('%', :#{#request.q}, '%'))
            AND
            (x.deleted = FALSE)
             """)
    Page<Address> findAllAddress(@Param("customerId") String customerId, @Param("request") ClientAddressRequest request, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(x) > 0 THEN TRUE ELSE FALSE END FROM Address x WHERE x.customer = :customer")
    boolean existsByCustomer(@Param("customer") Customer customer);

    @Query("SELECT x FROM Address x WHERE x.customer.id = :customerId AND x.isDefault = TRUE")
    Address findDefaultAddressByCustomer(@Param("customerId") String customerId);


    List<Address> findByCustomerAndDeleted(Customer customer,Boolean deleted);

    @Modifying
    @Transactional
    @Query("DELETE FROM Address x WHERE x.id IN (SELECT y.address.id FROM Order y WHERE y.id IN :orderIds)")
    void deleteAddressByOrder(List<String> orderIds);

    Optional<Address> findByIdAndCustomer(String id, Customer customer);
}
