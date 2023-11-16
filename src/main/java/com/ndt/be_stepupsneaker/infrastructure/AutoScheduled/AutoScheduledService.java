package com.ndt.be_stepupsneaker.infrastructure.AutoScheduled;

public interface AutoScheduledService {

    void updateVoucherStatusAutomatically();

    void updateOrderAutomatically();

    void updatePromotionStatusAutomatically();
}
