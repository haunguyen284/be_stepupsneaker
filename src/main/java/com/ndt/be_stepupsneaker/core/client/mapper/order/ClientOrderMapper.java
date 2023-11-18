package com.ndt.be_stepupsneaker.core.client.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderResponse;
import com.ndt.be_stepupsneaker.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface ClientOrderMapper {
    ClientOrderMapper INSTANCE = Mappers.getMapper( ClientOrderMapper.class );

    ClientOrderResponse orderToClientOrderResponse(Order order);

    @Mapping(target = "customer.id", source = "customer")
    @Mapping(target = "employee.id", source = "employee")
    @Mapping(target = "voucher.id", source = "voucher")
    @Mapping(target = "address.id", source = "address")
    Order clientOrderRequestToOrder(ClientOrderRequest orderRequest);

}
