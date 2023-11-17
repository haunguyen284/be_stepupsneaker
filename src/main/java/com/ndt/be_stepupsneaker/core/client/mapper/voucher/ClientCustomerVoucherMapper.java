package com.ndt.be_stepupsneaker.core.client.mapper.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.voucher.AdminCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientCustomerVoucherRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.voucher.ClientCustomerVoucherResponse;
import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientCustomerVoucherMapper {

    ClientCustomerVoucherMapper INSTANCE = Mappers.getMapper( ClientCustomerVoucherMapper.class );



    ClientCustomerVoucherResponse customerVoucherToClientCustomerVoucherResponse(CustomerVoucher customerVoucher);

    @Mapping(target = "customer.id", source = "customer")
    @Mapping(target = "voucher.id", source = "voucher")
    CustomerVoucher clientCustomerVoucherRequestToCustomerVoucher(ClientCustomerVoucherRequest customerVoucherRequest);
}
