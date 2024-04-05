package com.ndt.be_stepupsneaker.core.client.mapper.order;

import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientReturnFormResponse;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientReturnFormMapper {
    ClientReturnFormMapper INSTANCE = Mappers.getMapper( ClientReturnFormMapper.class );
    ClientReturnFormResponse returnFormToClientReturnFormResponse(ReturnForm returnForm);
}
