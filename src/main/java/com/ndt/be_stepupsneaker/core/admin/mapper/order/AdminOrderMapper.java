package com.ndt.be_stepupsneaker.core.admin.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderResponse;
import com.ndt.be_stepupsneaker.entity.order.Order;
import org.aspectj.apache.bcel.generic.TABLESWITCH;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface AdminOrderMapper {
    AdminOrderMapper INSTANCE = Mappers.getMapper( AdminOrderMapper.class );

    AdminOrderResponse orderToAdminOrderResponse(Order order);

    @Mapping(target = "customer.id", source = "customer")
    @Mapping(target = "voucher.id", source = "voucher")
    @Mapping(target = "address.id", source = "address")
    @Mapping(target = "payments", ignore = true)
    Order adminOrderRequestToOrder(AdminOrderRequest orderRequest);

}
