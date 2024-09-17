package org.shopping.repository;

import org.shopping.entity.Account;
import org.shopping.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, Integer> {

    @Query("select p from Product  p where" +
            "(:likeName is null or lower(p.code) like lower(concat('%', :likeName, '%')))")
    Page<Product> search(@Param("likeName") String likeName, Pageable pageable);
}
