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
public interface BaseUtilRepository<T> extends JpaRepository<T, String> {

    @Modifying
    @Query("UPDATE #{#entityName} e " +
            "SET e.status = " +
            "   CASE " +
            "       WHEN :currentTime < e.startDate AND :currentTime < e.endDate THEN 3 " +
            "       WHEN :currentTime BETWEEN e.startDate AND e.endDate THEN 0 " +
            "       WHEN :currentTime > e.endDate THEN 1 " +
            "       ELSE 2 " +
            "   END " +
            "WHERE e.deleted = FALSE AND e.status != 4")
    @Transactional
    void updateStatusAutomatically(@Param("currentTime") Long currentTime);

    @Modifying
    @Query("UPDATE #{#entityName} x " +
            "SET x.status = "+
            "   CASE " +
            "       WHEN (:startDate < CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP < :endDate) THEN 0 "+
            "       WHEN (:startDate > CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP < :endDate) THEN 3 " +
            "       ELSE 1 " +
            "   END "+
            "WHERE x.id = :id")
    void updateStatusBasedOnTime(@Param("id") String id,
                                 @Param("startDate") Long startDate,
                                 @Param("endDate") Long endDate);
}
