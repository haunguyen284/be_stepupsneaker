package com.ndt.be_stepupsneaker.core.client.mapper.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientCustomerMapper {
    ClientCustomerMapper INSTANCE = Mappers.getMapper(ClientCustomerMapper.class);


    AdminCustomerResponse customerToAdminCustomerResponse(Customer customer);

    Customer adminCustomerRequestToCustomer(AdminCustomerRequest customerDTO);
}
