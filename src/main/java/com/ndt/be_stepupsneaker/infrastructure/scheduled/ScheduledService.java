package com.ndt.be_stepupsneaker.infrastructure.scheduled;

public interface ScheduledService {

    void updateVoucherStatusAutomatically();

    void updateOrderAutomatically();

    void deleteCartDetailsByDate();

    void deleteOrderAutomaticallyByTypeAndStatus();
}
