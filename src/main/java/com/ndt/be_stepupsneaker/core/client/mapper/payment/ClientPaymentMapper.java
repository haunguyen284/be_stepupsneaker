package com.ndt.be_stepupsneaker.core.client.mapper.payment;

import com.ndt.be_stepupsneaker.core.client.dto.request.payment.ClientPaymentRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.payment.ClientPaymentResponse;
import com.ndt.be_stepupsneaker.entity.payment.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientPaymentMapper {

    ClientPaymentMapper INSTANCE = Mappers.getMapper( ClientPaymentMapper.class );

    ClientPaymentResponse paymentToClientPaymentResponse(Payment Payment);

    @Mapping(target = "order.id", source = "order")
    @Mapping(target = "paymentMethod.id", source = "paymentMethod")
    Payment clientPaymentRequestToPayment(ClientPaymentRequest PaymentRequest);

}
