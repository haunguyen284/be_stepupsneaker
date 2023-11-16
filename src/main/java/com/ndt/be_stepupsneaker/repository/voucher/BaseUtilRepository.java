package com.ndt.be_stepupsneaker.repository.voucher;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

@Transactional
@NoRepositoryBean
public interface BaseUtilRepository<T> extends JpaRepository<T, UUID> {

    @Modifying
    @Query("UPDATE #{#entityName} e " +
            "SET e.status = " +
            "   CASE " +
            "       WHEN :currentTime < e.startDate THEN 1 " +
            "       WHEN :currentTime BETWEEN e.startDate AND e.endDate THEN 0 " +
            "       ELSE 2 " +
            "   END " +
            "WHERE e.deleted = FALSE")
    @Transactional
    void updateStatusAutomatically(@Param("currentTime") Long currentTime);
}
