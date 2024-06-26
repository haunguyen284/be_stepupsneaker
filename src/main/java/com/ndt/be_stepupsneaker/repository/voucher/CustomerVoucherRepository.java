package com.ndt.be_stepupsneaker.repository.voucher;

import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository(CustomerVoucherRepository.NAME)
public interface CustomerVoucherRepository extends JpaRepository<CustomerVoucher, String> {
    public static final String NAME = "BaseCustomerVoucherRepository";
}
