package com.ndt.be_stepupsneaker.core.client.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientOrderDetailResponse;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientOrderDetailMapper {
    ClientOrderDetailMapper INSTANCE = Mappers.getMapper( ClientOrderDetailMapper.class );

    ClientOrderDetailResponse orderDetailToClientOrderDetailResponse(OrderDetail orderDetail);

    @Mapping(target = "productDetail.id", source = "productDetail")
    @Mapping(target = "order.id", source = "order")
    OrderDetail clientOrderDetailRequestToOrderDetail(ClientOrderDetailRequest orderDetailRequest);

}
