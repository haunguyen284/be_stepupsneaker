package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.ReturnFormHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(ReturnFormHistoryRepository.NAME)
public interface ReturnFormHistoryRepository extends JpaRepository<ReturnFormHistory, String> {
    public static final String NAME = "BaseReturnFormHistoryRepository";
}