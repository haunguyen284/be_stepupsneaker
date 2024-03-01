package com.ndt.be_stepupsneaker.infrastructure.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ScheduledController {

    private final ScheduledService scheduledService;

    @Scheduled(cron = "0 * * * * ?") // chạy 1 phút 1 lần...
    public void updateDiscountStatusDaily() {
        scheduledService.updateVoucherStatusAutomatically();
        scheduledService.updateOrderAutomatically();
        scheduledService.deleteOrderAutomaticallyByTypeAndStatus();
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Chạy vào ngày đầu tiên của mỗi tháng
    public void deleteOldCartDetails() {
        scheduledService.deleteCartDetailsByDate();
    }
}
