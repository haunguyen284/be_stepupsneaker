package com.ndt.be_stepupsneaker.core.admin.repository.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
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
    Optional<Address> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT x FROM Address x WHERE (x.phoneNumber = :phoneNumber AND :phoneNumber IN ('SELECT y.phoneNumber FROM Address y WHERE y.id != :id'))
            """)
    Optional<Address> findByPhoneNumber(@Param("id") String id, @Param("phoneNumber") String phoneNumber);


    @Query("""
            SELECT x FROM Address x 
            WHERE (:customerId IS NULL OR x.customer.id  = :customerId)
            AND 
            (:#{#request.wardName} IS NULL OR :#{#request.wardName} ILIKE '' OR x.wardName ILIKE  CONCAT('%', :#{#request.wardName}, '%')) 
            AND 
            (:#{#request.provinceName} IS NULL OR :#{#request.provinceName} ILIKE '' OR x.provinceName ILIKE  CONCAT('%', :#{#request.provinceName}, '%')) 
            AND 
            (:#{#request.districtName} IS NULL OR :#{#request.districtName} ILIKE '' OR x.districtName ILIKE  CONCAT('%', :#{#request.districtName}, '%')) 
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
    Page<Address> findAllAddress(@Param("customerId") String customerId, @Param("request") AdminAddressRequest request, Pageable pageable);


}
