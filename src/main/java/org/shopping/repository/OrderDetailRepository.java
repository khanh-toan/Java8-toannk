package org.shopping.repository;

import org.shopping.entity.OrderDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends BaseRepository<OrderDetails, Integer>{
}
