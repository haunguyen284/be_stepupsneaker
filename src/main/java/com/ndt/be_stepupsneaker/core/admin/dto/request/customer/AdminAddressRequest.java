package com.ndt.be_stepupsneaker.core.admin.dto.request.customer;

import com.ndt.be_stepupsneaker.core.common.base.PageableRequest;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.infrastructure.constant.EntityProperties;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddressRequest extends PageableRequest {
    private UUID id;
    private String phoneNumber;
    private Boolean isDefault;
    private String district;
    private String province;
    private String ward;
    private String more;
    private AdminCustomerRequest customerRequest;
}
