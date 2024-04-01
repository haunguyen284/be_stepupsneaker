package com.ndt.be_stepupsneaker.core.admin.repository.order;

import com.ndt.be_stepupsneaker.core.admin.dto.request.order.AdminReturnFormRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminBrandRequest;
import com.ndt.be_stepupsneaker.entity.order.ReturnFormHistory;
import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.order.ReturnFormHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface AdminReturnFormHistoryRepository extends ReturnFormHistoryRepository {
    @Query("""
    SELECT x FROM ReturnFormHistory x 
    WHERE 
    x.returnForm.id = :returnFormId 
    AND 
    x.deleted=false
    """)
    Page<ReturnFormHistory> findAllEntity(@Param("returnFormId") String returnFormId, Pageable pageable);

}
