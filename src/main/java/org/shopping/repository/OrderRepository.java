package org.shopping.repository;

import org.shopping.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseRepository<Order, Integer>{

    @Query("select max(o.orderNum) from Order o")
    Integer maxOrderNum();

    @Query("select o from Order o where" +
            "(:likeName is null or lower(o.name) like lower(concat('%', :likeName, '%'))) and" +
            "(:userId is null or o.userId = :userId) and" +
            "(:active is null or o.isDeleted = :active)")
    Page<Order> search(@Param("likeName") String likeName,
                       @Param("userId") Integer userId,
                       @Param("active") Boolean active,
                       Pageable pageable);

    @Query("select case when count(*) > 0 then true else false end " +
            "from Order o join OrderDetails od on o.id = od.order.id " +
            "where o.userId = :userId and od.product.id = :productId and o.status = 'DELIVERED'")
    boolean existsByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
}
