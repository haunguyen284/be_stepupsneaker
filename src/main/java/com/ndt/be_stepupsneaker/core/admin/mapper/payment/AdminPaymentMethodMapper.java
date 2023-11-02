package com.ndt.be_stepupsneaker.core.admin.mapper.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentMethodRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentMethodResponse;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminPaymentMethodMapper {

    AdminPaymentMethodMapper INSTANCE = Mappers.getMapper( AdminPaymentMethodMapper.class );

    AdminPaymentMethodResponse paymentToAdminPaymentMethodResponse(PaymentMethod PaymentMethod);

    PaymentMethod adminPaymentMethodRequestToPaymentMethod(AdminPaymentMethodRequest PaymentMethodRequest);

}
