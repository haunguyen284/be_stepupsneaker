package com.ndt.be_stepupsneaker.core.admin.service.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.List;
import java.util.UUID;

public interface AdminCustomerVoucherService extends BaseService<AdminCustomerVoucherResponse, String, AdminCustomerVoucherRequest> {
    List<AdminCustomerVoucherResponse> createCustomerVoucher(String voucher, List<String> customerIds);

    Boolean deleteCustomersByVoucherIdAndCustomerIds(String voucherId, List<String> customerIds);
}
