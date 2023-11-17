package com.ndt.be_stepupsneaker.core.client.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientVoucherResponse;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientVoucherMapper {

    ClientVoucherMapper INSTANCE = Mappers.getMapper( ClientVoucherMapper.class );

    ClientVoucherResponse voucherToClientVoucherResponse(Voucher voucher);

    Voucher clientVoucherRequestToVoucher(ClientVoucherHistoryRequest voucherRequest);
}
