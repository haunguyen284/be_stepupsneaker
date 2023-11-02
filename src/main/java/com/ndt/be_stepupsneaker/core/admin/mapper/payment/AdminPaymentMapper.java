package com.ndt.be_stepupsneaker.core.admin.mapper.payment;

import com.ndt.be_stepupsneaker.core.admin.dto.request.payment.AdminPaymentRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.payment.AdminPaymentResponse;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminPaymentMapper {

    AdminPaymentMapper INSTANCE = Mappers.getMapper( AdminPaymentMapper.class );

    AdminPaymentResponse paymentToAdminPaymentResponse(Payment Payment);

    @Mapping(target = "order.id", source = "order")
    @Mapping(target = "paymentMethod.id", source = "paymentMethod")
    Payment adminPaymentRequestToPayment(AdminPaymentRequest PaymentRequest);

}
