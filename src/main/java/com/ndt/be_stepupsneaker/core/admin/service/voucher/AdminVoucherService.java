package com.ndt.be_stepupsneaker.core.admin.service.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminColorResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.entity.customer.Customer;

import java.util.List;
import java.util.UUID;

public interface AdminVoucherService extends BaseService<AdminVoucherResponse, UUID, AdminVoucherRequest> {
    Boolean removeCustomersFromVoucher(UUID voucherId, List<UUID> customerIdToRemove);

    void updateVoucherStatusAutomatically();

    // Function not user this file
    List<AdminCustomerResponse> getAllCustomerByVoucherId(UUID voucherId);


}
