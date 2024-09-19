package org.shopping.service;

import lombok.RequiredArgsConstructor;
import org.shopping.common.ConflictException;
import org.shopping.common.NameAlreadyExistsException;
import org.shopping.entity.Account;
import org.shopping.entity.Product;
import org.shopping.repository.ProductRepository;
import org.shopping.utils.ConvertUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getProducts(String likeName, Boolean active, int currentPage, int size) {
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        Page<Product> products = productRepository.search(likeName, active, pageable);
        return products;
    }

    public Optional<Product> selectById(Integer id) {
        return productRepository.findById(id);
    }

    public String getImageContentType(byte[] imageData) {
        // Sử dụng một cách kiểm tra đơn giản dựa trên byte array
        if (imageData.length > 4) {
            // Kiểm tra các magic numbers cơ bản của file ảnh
            if ((imageData[0] & 0xFF) == 0xFF && (imageData[1] & 0xFF) == 0xD8) {
                return "image/jpeg"; // JPEG
            } else if ((imageData[0] & 0xFF) == 0x89 && new String(imageData, 1, 3).equals("PNG")) {
                return "image/png"; // PNG
            } else if (new String(imageData, 0, 3).equals("GIF")) {
                return "image/gif"; // GIF
            }
        }
        return null;
    }

    public void updateProduct(Product product) {
        Optional<Product> productFromDB = productRepository.findById(product.getId());
        if (productFromDB.isPresent()){
            Product productUpdate = productFromDB.get();
            productUpdate.setUpdatedAt(new Date());
            productUpdate.setName(product.getName());
            productUpdate.setIsDeleted(product.getIsDeleted());
            productUpdate.setPrice(product.getPrice());
            if (product.getImage() != null){
                productUpdate.setImage(product.getImage());
            }
            productRepository.save(productUpdate);
        }
    }

    public void createProduct(Product createProduct) {
        Product exception = productRepository.findByCode(createProduct.getCode());
        if(exception != null){
            throw new NameAlreadyExistsException("Code");
        }
        productRepository.save(createProduct);
    }
}
