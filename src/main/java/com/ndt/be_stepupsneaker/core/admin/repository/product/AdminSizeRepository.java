package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSizeRequest;
import com.ndt.be_stepupsneaker.entity.product.Size;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.SizeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminSizeRepository extends SizeRepository {
    @Query("""
            SELECT x FROM Size x 
            WHERE (:#{#request.name} IS NULL OR :#{#request.name} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.name}, '%'))
            AND 
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR x.name ILIKE  CONCAT('%', :#{#request.q}, '%')) 
            AND 
            (:status IS NULL OR x.status = :status)
            AND
            (x.deleted = FALSE)
            """)
    Page<Size> findAllSize(@Param("request") AdminSizeRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);

    @Query("""
            SELECT x FROM Size x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Color y WHERE y.id != :id))
            """)
    Optional<Size> findByName(@Param("id") String id, @Param("name") String name);

    Optional<Size> findByName(String name);

}
