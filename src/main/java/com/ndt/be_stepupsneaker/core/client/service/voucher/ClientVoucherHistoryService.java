package com.ndt.be_stepupsneaker.core.client.service.voucher;

import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

public interface ClientVoucherHistoryService {
    PageableObject<ClientVoucherHistoryResponse> findAllEntity(ClientVoucherHistoryRequest request);
}
