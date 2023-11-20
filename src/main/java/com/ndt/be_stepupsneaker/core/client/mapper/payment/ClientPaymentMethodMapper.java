package com.ndt.be_stepupsneaker.core.client.mapper.payment;

import com.ndt.be_stepupsneaker.core.client.dto.request.payment.ClientPaymentMethodRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.payment.ClientPaymentMethodResponse;
import com.ndt.be_stepupsneaker.entity.payment.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientPaymentMethodMapper {

    ClientPaymentMethodMapper INSTANCE = Mappers.getMapper( ClientPaymentMethodMapper.class );

    ClientPaymentMethodResponse paymentToClientPaymentMethodResponse(PaymentMethod PaymentMethod);

    PaymentMethod clientPaymentMethodRequestToPaymentMethod(ClientPaymentMethodRequest PaymentMethodRequest);

}
