package com.ndt.be_stepupsneaker.core.admin.service.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyStatisticResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;

import java.util.UUID;

public interface AdminOrderService extends BaseService<AdminOrderResponse, String, AdminOrderRequest> {

    AdminDailyStatisticResponse getDailyRevenueBetween(Long start, Long end);

    AdminDailyStatisticResponse getDailyOrdersBetween(Long start, Long end);

}
