package com.ndt.be_stepupsneaker.infrastructure.AutoScheduled;

import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class AutoScheduledController {
    @Autowired
    private AutoScheduledService autoScheduledService;

    @Scheduled(cron = "0 * * * * ?") // chạy 1 phút 1 lần...
    public void updateDiscountStatusDaily() {
        autoScheduledService.updateVoucherStatusAutomatically();
        autoScheduledService.updateOrderAutomatically();
    }
}
