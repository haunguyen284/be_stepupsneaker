package com.ndt.be_stepupsneaker.core.admin.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminCustomerVoucherMapper {

    AdminCustomerVoucherMapper INSTANCE = Mappers.getMapper( AdminCustomerVoucherMapper.class );

    @Mapping(target = "voucherResponse", source = "voucher")
    @Mapping(target = "customerResponse", source = "customer")
    AdminCustomerVoucherResponse customerVoucherToAdminCustomerVoucherResponse(CustomerVoucher customerVoucher);

    CustomerVoucher adminCustomerVoucherRequestToCustomerVoucher(AdminCustomerVoucherRequest customerVoucherRequest);
}
