package com.ndt.be_stepupsneaker.repository.voucher;

import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(VoucherHistoryRepository.NAME)
public interface VoucherHistoryRepository extends JpaRepository<VoucherHistory, UUID> {
    public static final String NAME = "BaseVoucherHistoryRepository";

}
