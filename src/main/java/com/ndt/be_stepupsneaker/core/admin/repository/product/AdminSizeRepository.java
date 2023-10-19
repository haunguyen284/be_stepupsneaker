package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminColorRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.request.product.AdminSizeRequest;
import com.ndt.be_stepupsneaker.core.admin.dto.response.product.AdminSizeResponse;
import com.ndt.be_stepupsneaker.entity.product.Color;
import com.ndt.be_stepupsneaker.entity.product.Size;
import com.ndt.be_stepupsneaker.infrastructure.constant.ProductPropertiesStatus;
import com.ndt.be_stepupsneaker.repository.product.ColorRepository;
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
            WHERE (:#{#request.name} IS NULL OR :#{#request.name} LIKE '' OR x.name LIKE  CONCAT('%', :#{#request.name}, '%'))
            AND
            (:status IS NULL OR x.status = :status)
            AND
            (x.deleted = FALSE)
            """)
    Page<Size> findAllSize(@Param("request") AdminSizeRequest request, @Param("status") ProductPropertiesStatus status, Pageable pageable);

    @Query("""
            SELECT x FROM Size x WHERE (x.name = :name AND :name IN (SELECT y.name FROM Color y WHERE y.id != :id))
            """)
    Optional<Size> findByName(@Param("id") UUID id, @Param("name") String name);

    Optional<Size> findByName(String name);

}