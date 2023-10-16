package com.ndt.be_stepupsneaker.core.admin.repository.product;

import com.ndt.be_stepupsneaker.entity.product.Brand;
import com.ndt.be_stepupsneaker.repository.product.BrandRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminBrandRepository extends BrandRepository {
//    @Query(value = """
//    SELECT c.id, c.name, c.status, c.created_at, c.updated_at, c.created_by, c.updated_by, c.deleted FROM brand as c
//    WHERE ( :#{#req.name} IS NULL OR :#{#req.name} LIKE '' OR c.name LIKE %:#{#req.name}% )
//    ORDER BY c.created_at
//    """, nativeQuery = true)
//    Page<Brand> findAllBrand(@Param("req") AdminBrandRequest request, Pageable pageable);

    Optional<Brand> findByName(String name);
}
