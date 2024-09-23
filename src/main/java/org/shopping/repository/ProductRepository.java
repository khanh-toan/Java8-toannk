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

    @Query("select p from Product p where" +
            "(:likeName is null or lower(p.code) like lower(concat('%', :likeName, '%'))) and" +
            "(:active is null or  p.isDeleted = :active)")
    Page<Product> search(@Param("likeName") String likeName,
                         @Param("active") Boolean active,
                         Pageable pageable);

    Product findByCode(String code);

    @Query("SELECT p.quantity FROM Product p WHERE p.id = :id")
    Integer findQuantityById(@Param("id") int id);

}
