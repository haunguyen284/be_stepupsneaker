package com.ndt.be_stepupsneaker.core.admin.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminColorResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminVoucherMapper {

    AdminVoucherMapper INSTANCE = Mappers.getMapper( AdminVoucherMapper.class );

    AdminVoucherResponse voucherToAdminVoucherResponse(Voucher voucher);

    Voucher adminVoucherRequestToVoucher(AdminVoucherRequest voucherRequest);
}
