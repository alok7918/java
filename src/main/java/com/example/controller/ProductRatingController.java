package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.RatingRequest;
import com.example.model.Product;
import com.example.model.ProductRating;
import com.example.model.User;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRatingRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;

@RestController
@RequestMapping("/api/ratings")
public class ProductRatingController {

    @Autowired
    private ProductRatingRepository ratingRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OrderRepository orderRepo;

    // ✅ Save or update rating
    @PostMapping("/{productId}")
    public ResponseEntity<String> rateProduct(
            @PathVariable Long productId,
            @RequestBody RatingRequest req,
            Principal principal) {

        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean purchased = orderRepo.existsByUsernameAndItemsProductId(user.getUsername(), productId);
        if (!purchased) {
            return ResponseEntity.status(403).body("You must buy this product to rate it.");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductRating rating = ratingRepo.findByProductIdAndUserId(productId, user.getId())
                .orElse(new ProductRating());

        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setComment(req.getComment());

        ratingRepo.save(rating);

        return ResponseEntity.ok("Rating saved");
    }

    // ✅ Get all ratings for product
    @GetMapping("/{productId}")
    public List<ProductRating> getProductRatings(@PathVariable Long productId) {
        return ratingRepo.findByProductId(productId);
    }

    // ✅ Check if user can rate
    @GetMapping("/{productId}/can-rate")
    public boolean canRate(@PathVariable Long productId, Principal principal) {
        String username = principal.getName();
        return orderRepo.existsByUsernameAndItemsProductId(username, productId);
    }

}
