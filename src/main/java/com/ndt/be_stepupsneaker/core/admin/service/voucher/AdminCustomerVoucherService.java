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

public interface AdminCustomerVoucherService extends BaseService<AdminCustomerVoucherResponse, UUID, AdminCustomerVoucherRequest> {
    List<AdminCustomerVoucherResponse> createCustomerVoucher(List<UUID> voucherIds, List<UUID> customerIds);

    Boolean deleteCustomersByVoucherIdAndCustomerIds(UUID voucherId, List<UUID> customerIds);
}
