package com.ndt.be_stepupsneaker.core.admin.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormHistoryResponse;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormResponse;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminReturnFormHistoryMapper {
    AdminReturnFormHistoryMapper INSTANCE = Mappers.getMapper( AdminReturnFormHistoryMapper.class );
    AdminReturnFormHistoryResponse returnFormHistoryToAdminReturnFormHistoryResponse(ReturnFormHistory returnFormHistory);
}

