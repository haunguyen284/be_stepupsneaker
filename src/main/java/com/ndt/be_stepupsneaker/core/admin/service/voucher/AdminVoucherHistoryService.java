package com.ndt.be_stepupsneaker.core.admin.service.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.UUID;

public interface AdminVoucherHistoryService {
    PageableObject<AdminVoucherHistoryResponse> findAllEntity(AdminVoucherHistoryRequest request);
}
