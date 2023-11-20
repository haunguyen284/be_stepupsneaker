package com.ndt.be_stepupsneaker.core.client.mapper.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderHistoryResponse;
import com.ndt.be_stepupsneaker.entity.order.OrderHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientOrderHistoryMapper {
    ClientOrderHistoryMapper INSTANCE = Mappers.getMapper( ClientOrderHistoryMapper.class );

    ClientOrderHistoryResponse orderHistoryToClientOrderHistoryResponse(OrderHistory orderHistory);

    @Mapping(target = "order.id", source = "order")
    OrderHistory clientOrderHistoryRequestToOrderHistory(ClientOrderDetailRequest orderHistoryRequest);

}
