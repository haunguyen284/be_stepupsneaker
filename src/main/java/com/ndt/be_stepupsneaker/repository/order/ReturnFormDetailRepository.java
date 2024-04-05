package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.ReturnFormDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(ReturnFormDetailRepository.NAME)
public interface ReturnFormDetailRepository extends JpaRepository<ReturnFormDetail, String> {
    public static final String NAME = "BaseReturnFormDetailRepository";
}