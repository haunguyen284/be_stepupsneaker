package com.ndt.be_stepupsneaker.core.admin.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminVoucherResponse;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminCustomerVoucherMapper {

    AdminCustomerVoucherMapper INSTANCE = Mappers.getMapper( AdminCustomerVoucherMapper.class );


    AdminCustomerVoucherResponse customerVoucherToAdminCustomerVoucherResponse(CustomerVoucher customerVoucher);

    CustomerVoucher adminCustomerVoucherRequestToCustomerVoucher(AdminCustomerVoucherRequest customerVoucherRequest);
}
