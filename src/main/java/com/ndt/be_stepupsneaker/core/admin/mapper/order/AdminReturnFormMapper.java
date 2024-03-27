package com.ndt.be_stepupsneaker.core.admin.mapper.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.order.AdminReturnFormResponse;
import com.ndt.be_stepupsneaker.entity.order.ReturnForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminReturnFormMapper {
    AdminReturnFormMapper INSTANCE = Mappers.getMapper( AdminReturnFormMapper.class );
    AdminReturnFormResponse returnFormToAdminReturnFormResponse(ReturnForm returnForm);

    ReturnForm adminReturnFormRequestToReturnForm(AdminReturnFormRequest adminReturnFormRequest);
}
