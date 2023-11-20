package com.ndt.be_stepupsneaker.core.admin.service.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.UUID;

public interface AdminVoucherService extends BaseService<AdminVoucherResponse, String, AdminVoucherRequest> {
    PageableObject<AdminVoucherResponse> findAllVoucher(AdminVoucherRequest voucherReq, String customerId, String noCustomerId);

}
