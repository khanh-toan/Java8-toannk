package org.shopping.repository;

import org.shopping.entity.Review;
import org.shopping.form.ReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT new org.shopping.form.ReviewDTO(r.rating, r.comment, r.createdAt, a.username) " +
            "FROM Review r JOIN r.user a JOIN r.product p WHERE p.id = :productId")
    List<ReviewDTO> findReviewsByProductId(@Param("productId") Integer productId);

}
