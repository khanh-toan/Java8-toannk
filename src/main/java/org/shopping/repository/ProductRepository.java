package org.shopping.repository;

import org.shopping.entity.Account;
import org.shopping.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, Integer> {

}
