package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.CartItem;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.repository.CartItemRepository;
import com.example.repository.OrderRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CartItemRepository cartItemRepository;


    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> placeOrder(@RequestParam String username,
                                        @RequestParam String paymentId) {

        List<CartItem> cartItems = cartItemRepository.findByUsername(username);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }

        Order order = new Order();
        order.setUsername(username);
        order.setPaymentId(paymentId);
        order.setStatus("PLACED");

        double total = 0;
        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            items.add(oi);
//            total += ci.getProduct().getPrice() * ci.getQuantity();
        }

        order.setItems(items);
        order.setTotalAmount(total);

        orderRepository.save(order);

        cartItemRepository.deleteAll(cartItems);

        return ResponseEntity.ok("Order placed successfully!");
    }

    // ✅ Get all orders for user
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<Order> getUserOrders(@RequestParam String username) {
        return orderRepository.findByUsername(username);
    }
}
