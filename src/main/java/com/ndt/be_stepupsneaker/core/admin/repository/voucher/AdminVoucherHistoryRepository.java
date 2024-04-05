package com.ndt.be_stepupsneaker.core.admin.repository.voucher;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.voucher.AdminVoucherHistoryRequest;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.entity.voucher.VoucherHistory;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.voucher.VoucherHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminVoucherHistoryRepository extends VoucherHistoryRepository {
    @Query("""
    SELECT x FROM VoucherHistory x 
    WHERE 
    (:#{#request.customer} IS NULL OR :#{#request.customer} ILIKE '' OR x.order.customer.id = :#{#request.customer}) 
    AND     
    x.deleted = FALSE
    """)
    Page<VoucherHistory> findAllVoucherHistory(@Param("request") AdminVoucherHistoryRequest request, Pageable pageable);

    @Modifying
    @Transactional
    void deleteAllByOrder(Order order);
}
