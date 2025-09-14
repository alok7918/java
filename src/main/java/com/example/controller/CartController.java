package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.CartItem;
import com.example.model.Product;
import com.example.repository.CartItemRepository;
import com.example.repository.ProductRepository;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam String username) {

        Product product = productRepository.findById(productId).orElseThrow();
        Optional<CartItem> existingOpt = cartItemRepository.findByUsernameAndProductId(username, productId);

        if (existingOpt.isPresent()) {
            // Product already in cart: update quantity
            CartItem existing = existingOpt.get();
            existing.setQuantity(existing.getQuantity() + quantity);
            cartItemRepository.save(existing);
        } else {
            // Add new cart item
            CartItem item = new CartItem();
            item.setUsername(username);
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        // ✅ NEW: Return updated cart count
        int cartCount = cartItemRepository.countByUsername(username);
        return ResponseEntity.ok(cartCount);
    }


    @GetMapping("/count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCartCount(@RequestParam String username) {
        int count = cartItemRepository.countByUsername(username);
        return ResponseEntity.ok(count);
    }



    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<CartItem> getUserCart(@RequestParam String username) {
        return cartItemRepository.findByUsername(username);
    }

    // ✅ New: Update quantity of cart item
    @PostMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateQuantity(
            @RequestParam Long id,
            @RequestParam int quantity) {
        CartItem item = cartItemRepository.findById(id).orElseThrow();
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return ResponseEntity.ok("Quantity updated");
    }

    // ✅ New: Checkout = clear cart for user
    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> checkout(@RequestParam String username) {
        List<CartItem> items = cartItemRepository.findByUsername(username);
        cartItemRepository.deleteAll(items);
        return ResponseEntity.ok("Checkout done!");
    }
}
