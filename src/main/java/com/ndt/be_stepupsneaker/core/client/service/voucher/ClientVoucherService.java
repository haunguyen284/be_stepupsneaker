package com.ndt.be_stepupsneaker.core.client.service.voucher;

import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherResponse;
import com.ndt.be_stepupsneaker.core.common.base.BaseService;
import com.ndt.be_stepupsneaker.core.common.base.PageableObject;

import java.util.List;

public interface ClientVoucherService extends BaseService<ClientVoucherResponse, String, ClientVoucherRequest> {
    PageableObject<ClientVoucherResponse> findAllVoucher(ClientVoucherRequest voucherReq, String customerId);

    PageableObject<ClientVoucherResponse> findLegitVouchers(String customerId, float totalMoney);

    ClientVoucherResponse findByCode(String code);
}
