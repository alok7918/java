package com.example.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    List<CartItem> findByUsername(String username);
    Optional<CartItem> findByUsernameAndProductId(String username, Long productId);
    int countByUsername(String username);
}
