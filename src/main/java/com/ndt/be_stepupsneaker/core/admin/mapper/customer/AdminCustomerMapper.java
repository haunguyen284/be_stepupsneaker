package com.ndt.be_stepupsneaker.core.admin.mapper.customer;

import com.ndt.be_stepupsneaker.core.admin.dto.request.customer.AdminCustomerRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.customer.AdminCustomerResponse;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminCustomerMapper {
    AdminCustomerMapper INSTANCE = Mappers.getMapper((AdminCustomerMapper.class));

    AdminCustomerResponse customerToAdminCustomerResponse(Customer customer);

    Customer adminCustomerRequestToCustomer(AdminCustomerRequest customerDTO);
}
