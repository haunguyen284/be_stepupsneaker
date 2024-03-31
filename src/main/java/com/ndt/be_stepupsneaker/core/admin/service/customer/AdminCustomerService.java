package com.ndt.be_stepupsneaker.core.admin.service.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyGrowthResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.statistic.AdminDailyStatisticResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import org.hibernate.validator.internal.engine.validationcontext.BaseBeanValidationContext;

import java.util.List;
import java.util.UUID;

public interface AdminCustomerService extends BaseService<AdminCustomerResponse, String, AdminCustomerRequest> {

    PageableObject<AdminCustomerResponse> findAllCustomer(AdminCustomerRequest customerRequest, String voucher, String noVoucher);

    AdminDailyStatisticResponse getDailyCustomersBetween(Long start, Long end);

    List<AdminDailyGrowthResponse> getCustomersGrowthBetween(Long start, Long end);

}
