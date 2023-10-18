package com.ndt.be_stepupsneaker.core.admin.mapper.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminAddressRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminAddressResponse;
import com.ndt.be_stepupsneaker.entity.customer.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminAddressMapper {

    AdminAddressMapper INSTANCE = Mappers.getMapper(AdminAddressMapper.class);

    AdminAddressResponse addressToAdminAddressResponse(Address address);

    Address adminAddressRequestAddress(AdminAddressRequest addressDTO);
}
