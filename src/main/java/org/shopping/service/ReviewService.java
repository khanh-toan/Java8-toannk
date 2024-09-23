package org.shopping.service;

import lombok.RequiredArgsConstructor;
import org.shopping.common.RecordNotfoundException;
import org.shopping.entity.Account;
import org.shopping.entity.Product;
import org.shopping.entity.Review;
import org.shopping.form.ReviewDTO;
import org.shopping.repository.AccountRepository;
import org.shopping.repository.ProductRepository;
import org.shopping.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    public void saveReview(ReviewDTO reviewDTO) {
        // Lấy product từ cơ sở dữ liệu
        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RecordNotfoundException("Product"));

        // Lấy user từ cơ sở dữ liệu
        Account user = accountRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new RecordNotfoundException("User"));
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(reviewDTO.getCreatedAt());  // Thêm thời gian tạo đánh giá
        reviewRepository.save(review);
    }
}
