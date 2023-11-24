package com.ndt.be_stepupsneaker.core.client.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.core.client.dto.request.voucher.ClientVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.repository.voucher.VoucherHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Transactional
@Repository
public interface ClientVoucherHistoryRepository extends VoucherHistoryRepository {
    @Query("""
    SELECT x FROM VoucherHistory x 
    WHERE 
    (:#{#request.customer} IS NULL OR :#{#request.customer} ILIKE '' OR x.order.customer.id = :#{#request.customer}) 
    AND 
    x.deleted=false
    """)
    Page<VoucherHistory> findAllVoucherHistory(@Param("request") ClientVoucherHistoryRequest request, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM VoucherHistory x WHERE x.order.id IN :orderIds")
    void deleteVoucherHistoryByOrder(List<String> orderIds);
}
