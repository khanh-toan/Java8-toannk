package org.shopping.service;

import lombok.RequiredArgsConstructor;
import org.shopping.entity.Account;
import org.shopping.entity.Product;
import org.shopping.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getProducts(String likeName, int currentPage, int size) {
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        Page<Product> products = productRepository.search(likeName, pageable);
        return products;
    }

}
