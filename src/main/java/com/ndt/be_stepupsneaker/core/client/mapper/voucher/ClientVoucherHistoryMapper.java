package com.ndt.be_stepupsneaker.core.client.mapper.voucher;

import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientVoucherHistoryMapper {
    ClientVoucherHistoryMapper INSTANCE = Mappers.getMapper( ClientVoucherHistoryMapper.class );

    ClientVoucherHistoryResponse voucherHistoryToClientVoucherHistoryResponse(VoucherHistory voucherHistory);

    VoucherHistory clientVoucherHistoryRequestToVoucherHistory(ClientVoucherHistoryRequest clientVoucherHistoryRequest);
}
