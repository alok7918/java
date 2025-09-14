package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.ProductRating;

public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {
    Optional<ProductRating> findByProductIdAndUserId(Long productId, Long userId);
    List<ProductRating> findByProductId(Long productId);
}
