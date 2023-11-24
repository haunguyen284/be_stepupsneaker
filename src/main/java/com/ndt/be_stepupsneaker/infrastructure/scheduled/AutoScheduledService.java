package com.ndt.be_stepupsneaker.infrastructure.scheduled;

public interface AutoScheduledService {

    void updateVoucherStatusAutomatically();

    void updateOrderAutomatically();

    void updatePromotionStatusAutomatically();

    void deleteCartDetailsByDate();

    void deleteOrderAutomaticallyByTypeAndStatus();
}
