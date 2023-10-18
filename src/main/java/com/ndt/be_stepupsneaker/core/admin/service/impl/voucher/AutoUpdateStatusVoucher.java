package com.ndt.be_stepupsneaker.core.admin.service.impl.voucher;

import com.ndt.be_stepupsneaker.core.admin.service.voucher.AdminVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AutoUpdateStatusVoucher {
    @Autowired
    private AdminVoucherService adminVoucherService;

    @Scheduled(cron = "0 * * * * ?") // chạy 1 phút 1 lần...
    public void updateDiscountStatusDaily() {
        adminVoucherService.updateVoucherStatusAutomatically();
    }
}
