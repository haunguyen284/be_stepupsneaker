package com.ndt.be_stepupsneaker.core.admin.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherHistoryResponse;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminVoucherHistoryMapper {
    AdminVoucherHistoryMapper INSTANCE = Mappers.getMapper( AdminVoucherHistoryMapper.class );

    AdminVoucherHistoryResponse voucherHistoryToAdminVoucherHistoryResponse(VoucherHistory voucherHistory);

    VoucherHistory adminVoucherHistoryRequestToVoucherHistory(AdminVoucherHistoryRequest adminVoucherHistoryRequest);
}
