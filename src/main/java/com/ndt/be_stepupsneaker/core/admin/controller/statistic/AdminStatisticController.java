package com.ndt.be_stepupsneaker.core.admin.controller.statistic;

import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyStatisticResponse;
import com.ndt.be_stepupsneaker.core.admin.service.customer.AdminCustomerService;
import com.ndt.be_stepupsneaker.core.admin.service.order.AdminOrderService;
import com.ndt.be_stepupsneaker.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/statistic")
public class AdminStatisticController {

    private final AdminOrderService adminOrderService;
    private final AdminCustomerService adminCustomerService;

    @Autowired
    public AdminStatisticController(
            AdminOrderService adminOrderService,
            AdminCustomerService adminCustomerService
    ) {
        this.adminOrderService = adminOrderService;
        this.adminCustomerService = adminCustomerService;
    }

    @GetMapping("/daily-revenue")
    public Object getDailyRevenueBetween(
            @Param("start") Long start,
            @Param("end") Long end
            ) {
        AdminDailyStatisticResponse dailyRevenueResponse = adminOrderService.getDailyRevenueBetween(start, end);
        return ResponseHelper.getResponse(dailyRevenueResponse, HttpStatus.OK);
    }

    @GetMapping("/daily-orders")
    public Object getDailyOrdersBetween(
            @Param("start") Long start,
            @Param("end") Long end
            ) {
        AdminDailyStatisticResponse dailyRevenueResponse = adminOrderService.getDailyOrdersBetween(start, end);
        return ResponseHelper.getResponse(dailyRevenueResponse, HttpStatus.OK);
    }

    @GetMapping("/daily-customers")
    public Object getDailyCustomersBetween(
            @Param("start") Long start,
            @Param("end") Long end
            ) {
        AdminDailyStatisticResponse dailyRevenueResponse = adminCustomerService.getDailyCustomersBetween(start, end);
        return ResponseHelper.getResponse(dailyRevenueResponse, HttpStatus.OK);
    }

}
