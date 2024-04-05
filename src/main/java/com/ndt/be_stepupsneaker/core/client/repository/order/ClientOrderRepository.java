package com.ndt.be_stepupsneaker.core.client.repository.order;

import com.ndt.be_stepupsneaker.core.client.dto.request.order.ClientOrderRequest;
import com.ndt.be_stepupsneaker.core.client.dto.response.review.OrderWithReviewCountResponse;
import com.ndt.be_stepupsneaker.core.common.base.Statistic;
import com.ndt.be_stepupsneaker.entity.order.Order;
import com.ndt.be_stepupsneaker.infrastructure.constant.OrderStatus;
import com.ndt.be_stepupsneaker.repository.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientOrderRepository extends OrderRepository {

    @Query("""
            SELECT o FROM Order o 
            WHERE 
            (
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR o.code ILIKE  CONCAT('%', :#{#request.q}, '%')) OR 
            (:#{#request.q} IS NULL OR :#{#request.q} ILIKE '' OR o.fullName ILIKE  CONCAT('%', :#{#request.q}, '%'))
            )
            AND 
            ((:status IS NULL) OR (o.status = :status)) 
            AND 
            ((:#{#request.customer} IS NULL) OR (o.customer.id = :#{#request.customer})) 
            AND
            o.deleted=false 
            """)
    Page<Order> findAllOrder(
            @Param("request") ClientOrderRequest request,
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    List<Order> findAllByStatusAndCreatedAtBefore(OrderStatus status, Long cutoffTime);

    Integer countAllByStatus(OrderStatus status);


    @Query(
            value = """
                    SELECT
                        extract(epoch from date_trunc('day', to_timestamp(created_at/1000))) AS date,
                        SUM(total_money) AS value
                    FROM
                        shop_order
                    WHERE
                        deleted = false AND
                        status = 4 AND
                        created_at >= :start AND
                        created_at <= :end
                    GROUP BY
                        date
                    ORDER BY
                        date
                    """,
            nativeQuery = true
    )
    List<Statistic> getDailyRevenueBetween(@Param("start") Long start, @Param("end") Long end);

    @Query(
            value = """
                    SELECT
                        extract(epoch from date_trunc('day', to_timestamp(created_at/1000))) AS date,
                        COUNT(*) AS value
                    FROM
                        shop_order
                    WHERE
                        deleted = false AND
                        status = 4 AND
                        created_at >= :start AND
                        created_at <= :end
                    GROUP BY
                        date
                    ORDER BY
                        date
                    """,
            nativeQuery = true
    )
    List<Statistic> getDailyOrderBetween(@Param("start") Long start, @Param("end") Long end);

    Optional<Order> findByIdAndCustomer_Id(String id, String customerId);

    @Query("""
                SELECT 
                x AS order,
                COUNT(r) AS countReview
                FROM Order x 
                LEFT JOIN x.orderDetails o 
                LEFT JOIN Review r ON r.productDetail = o.productDetail
                WHERE x.code = :code
                GROUP BY x
            """)
    Optional<OrderWithReviewCountResponse> findByCodeAndReviewCount(String code);

    @Query("""
                SELECT 
                x AS order,
                COUNT(r) AS countReview
                FROM Order x 
                LEFT JOIN x.orderDetails o 
                LEFT JOIN Review r ON r.productDetail = o.productDetail
                WHERE x.id = :id AND x.customer.id = :customer
                GROUP BY x
            """)
    Optional<OrderWithReviewCountResponse> findByIdAndReviewCount(String id,String customer);
}
