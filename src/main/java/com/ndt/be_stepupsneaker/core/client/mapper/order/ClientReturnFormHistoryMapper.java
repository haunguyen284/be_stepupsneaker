package com.ndt.be_stepupsneaker.core.client.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.core.client.dto.response.order.ClientReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientReturnFormHistoryMapper {
    ClientReturnFormHistoryMapper INSTANCE = Mappers.getMapper( ClientReturnFormHistoryMapper.class );
    ClientReturnFormHistoryResponse returnFormHistoryToClientReturnFormHistoryResponse(ReturnFormHistory returnFormHistory);
}

