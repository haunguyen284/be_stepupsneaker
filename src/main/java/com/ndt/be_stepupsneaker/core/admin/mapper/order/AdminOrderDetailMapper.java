package com.ndt.be_stepupsneaker.core.admin.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminOrderDetailRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminOrderDetailResponse;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminOrderDetailMapper {
    AdminOrderDetailMapper INSTANCE = Mappers.getMapper( AdminOrderDetailMapper.class );

    AdminOrderDetailResponse orderDetailToAdminOrderDetailResponse(OrderDetail orderDetail);

    @Mapping(target = "productDetail.id", source = "productDetail")
    @Mapping(target = "order.id", source = "order")
    OrderDetail adminOrderDetailRequestToOrderDetail(AdminOrderDetailRequest orderDetailRequest);

}
