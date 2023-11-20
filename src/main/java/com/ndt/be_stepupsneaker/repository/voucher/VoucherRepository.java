package com.ndt.be_stepupsneaker.repository.voucher;

import com.ndt.be_stepupsneaker.entity.voucher.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(VoucherRepository.NAME)
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    public static final String NAME = "BaseVoucherRepository";
}
