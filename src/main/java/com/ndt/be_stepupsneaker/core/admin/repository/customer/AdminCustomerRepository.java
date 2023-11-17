package com.ndt.be_stepupsneaker.core.admin.repository.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.common.base.Statistic;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.constant.CustomerStatus;
import com.ndt.be_stepupsneaker.repository.customer.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface AdminCustomerRepository extends CustomerRepository {
    @Query("""
    SELECT x FROM Customer x
    WHERE
        (:#{#request.q} IS NULL OR :#{#request.q} ILIKE ''  OR x.fullName ILIKE CONCAT('%', :#{#request.q}, '%')
        OR
        x.email ILIKE CONCAT('%', :#{#request.q}, '%') OR x.gender ILIKE CONCAT('%', :#{#request.q}, '%')
        OR
        EXISTS
        (SELECT 1 FROM x.addressList a WHERE a.wardName ILIKE CONCAT('%', :#{#request.q}, '%')
        OR
        a.districtName ILIKE CONCAT('%', :#{#request.q}, '%')OR a.provinceName ILIKE CONCAT('%', :#{#request.q}, '%')
        OR
        a.more ILIKE CONCAT('%', :#{#request.q}, '%')OR a.phoneNumber ILIKE CONCAT('%', :#{#request.q}, '%')))
        AND
        (:status IS NULL OR x.status = :status)
        AND
        (:#{#request.dateOfBirth} IS NULL OR x.dateOfBirth = :#{#request.dateOfBirth})
        AND
        (CAST(:voucher as java.util.UUID) IS NULL OR x.id IN (SELECT o.customer.id FROM CustomerVoucher o WHERE o.voucher.id = CAST(:voucher as java.util.UUID)))
        AND
        (CAST(:noVoucher as java.util.UUID) IS NULL OR x.id NOT IN (SELECT o.customer.id FROM CustomerVoucher o WHERE o.voucher.id = CAST(:noVoucher as java.util.UUID)))
        AND
        (x.deleted = FALSE)
    """)
    Page<Customer> findAllCustomer(@Param("request") AdminCustomerRequest request,
                                   @Param("voucher") UUID voucher,
                                   @Param("noVoucher") UUID noVoucher,
                                   @Param("status") CustomerStatus status, Pageable pageable);

    Optional<Customer> findByEmail(String email);

    @Query("""
            SELECT x FROM Customer x WHERE (x.email = :email AND :email IN ('SELECT y.email FROM Customer y WHERE y.id != :id'))
            """)
    Optional<Customer> findByEmail(@Param("id") UUID id, @Param("email") String email);

    @Query(
            value = """
                SELECT
                    extract(epoch from date_trunc('day', to_timestamp(created_at/1000))) AS date,
                    COUNT(*) AS value
                FROM
                    customer
                WHERE
                    deleted = false AND
                    created_at >= :start AND
                    created_at <= :end
                GROUP BY
                    date
                ORDER BY
                    date
                """,
            nativeQuery = true
    )
    List<Statistic> getDailyCustomerBetween(@Param("start") Long start, @Param("end") Long end);
}
