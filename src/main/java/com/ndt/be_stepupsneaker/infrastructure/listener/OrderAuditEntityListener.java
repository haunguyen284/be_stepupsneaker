package com.ndt.be_stepupsneaker.infrastructure.listener;

import com.ndt.be_stepupsneaker.entity.order.Order;
import jakarta.persistence.PostLoad;
import org.springframework.util.SerializationUtils;

public class OrderAuditEntityListener extends AuditEntityListener{

    @PostLoad
    private void saveState(Order order){
        order.setPreviousOrderState(SerializationUtils.clone(order));
    }

}
