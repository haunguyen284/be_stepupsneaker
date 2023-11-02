package com.ndt.be_stepupsneaker.core.admin.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderHistoryRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderHistoryResponse;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminOrderHistoryMapper {

    AdminOrderHistoryMapper INSTANCE = Mappers.getMapper( AdminOrderHistoryMapper.class );

    AdminOrderHistoryResponse orderHistoryToAdminOrderHistoryResponse(OrderHistory orderHistory);

    @Mapping(target = "order.id", source = "order")
    OrderHistory adminOrderHistoryRequestToOrderHistory(AdminOrderHistoryRequest orderHistoryRequest);

}
