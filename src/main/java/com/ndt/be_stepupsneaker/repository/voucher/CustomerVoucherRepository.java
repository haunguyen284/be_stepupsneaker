package com.ndt.be_stepupsneaker.repository.voucher;

import com.ndt.be_stepupsneaker.entity.voucher.CustomerVoucher;
import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(CustomerVoucherRepository.NAME)
public interface CustomerVoucherRepository extends JpaRepository<CustomerVoucher, UUID> {
    public static final String NAME = "BaseCustomerVoucherRepository";
}
