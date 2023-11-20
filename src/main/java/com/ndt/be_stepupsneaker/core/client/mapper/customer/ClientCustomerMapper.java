package com.ndt.be_stepupsneaker.core.client.mapper.customer;

import com.ndt.be_stepupsneaker.core.client.dto.request.customer.ClientCustomerRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.customer.ClientCustomerResponse;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientCustomerMapper {
    ClientCustomerMapper INSTANCE = Mappers.getMapper(ClientCustomerMapper.class);


    ClientCustomerResponse customerToClientCustomerResponse(Customer customer);

    Customer clientCustomerRequestToCustomer(ClientCustomerRequest customerDTO);
}
