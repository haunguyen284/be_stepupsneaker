package com.ndt.be_stepupsneaker.core.admin.repository.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import com.ndt.be_stepupsneaker.repository.customer.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminAddressRepository extends AddressRepository {
    @Query("""
            SELECT x FROM Address x 
            WHERE
            (:#{#request.q} IS NULL OR :#{#request.q} LIKE '' OR x.phoneNumber LIKE CONCAT('%', :#{#request.q}, '%')  OR x.city LIKE CONCAT('%', :#{#request.q}, '%')OR x.province LIKE CONCAT('%', :#{#request.q}, '%')OR x.country LIKE CONCAT('%', :#{#request.q}, '%'))
            AND(x.deleted=FALSE)
            """)
    Page<Address> findAllAddress(@Param("request") AdminAddressRequest request, Pageable pageable);

    Optional<Address> findByPhoneNumber(String phoneNumber);

    @Query("""
            SELECT x FROM Address x WHERE (x.phoneNumber = :phoneNumber AND :phoneNumber IN ('SELECT y.phoneNumber FROM Address y WHERE y.id != :id'))
            """)
    Optional<Address> findByPhoneNumber(@Param("id") UUID id, @Param("phoneNumber") String phoneNumber);
}
