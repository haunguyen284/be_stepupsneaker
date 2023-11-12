package com.ndt.be_stepupsneaker.entity.voucher;

import com.ndt.be_stepupsneaker.entity.base.PrimaryEntity;
import com.ndt.be_stepupsneaker.entity.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "shop_voucher_history")
@Entity
public class VoucherHistory extends PrimaryEntity {
    @JoinColumn(name = "shop_order_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "shop_voucher_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Voucher voucher;

    @Column(name = "money_before_reduction")
    private float moneyBeforeReduction;

    @Column(name = "money_after_reduction")
    private float moneyAfterReduction;

    @Column(name = "money_reduction")
    private float moneyReduction;
}
