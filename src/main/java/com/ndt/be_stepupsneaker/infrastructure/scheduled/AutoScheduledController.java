package com.ndt.be_stepupsneaker.infrastructure.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AutoScheduledController {

    private final AutoScheduledService autoScheduledService;

    @Scheduled(cron = "0 * * * * ?") // chạy 1 phút 1 lần...
    public void updateDiscountStatusDaily() {
        autoScheduledService.updateVoucherStatusAutomatically();
        autoScheduledService.updateOrderAutomatically();
        autoScheduledService.updatePromotionStatusAutomatically();
        autoScheduledService.deleteOrderAutomaticallyByTypeAndStatus();
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Chạy vào ngày đầu tiên của mỗi tháng
    public void deleteOldCartDetails() {
        autoScheduledService.deleteCartDetailsByDate();
    }
}
