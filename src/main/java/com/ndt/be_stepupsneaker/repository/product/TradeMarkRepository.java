package com.ndt.be_stepupsneaker.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Style;
import com.ndt.be_stepupsneaker.entity.product.TradeMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;



@Repository(TradeMarkRepository.NAME)

public interface TradeMarkRepository extends JpaRepository<TradeMark, UUID> {
    public static final String NAME = "BaseTradeMarkRepository";
}
