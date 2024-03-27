package com.ndt.be_stepupsneaker.repository.order;

import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(ReturnFormRepository.NAME)
public interface ReturnFormRepository extends JpaRepository<ReturnForm, String> {
    public static final String NAME = "BaseReturnFormRepository";
}
