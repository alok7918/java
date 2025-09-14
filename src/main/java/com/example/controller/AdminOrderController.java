
package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Order;
import com.example.repository.OrderRepository;
@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;




    @GetMapping
    public Page<Order> getOrdersPaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "2") int size) {

        return orderRepository.findAll(
            PageRequest.of(page, size, Sort.by("id").descending())
        );
    }




@PutMapping("/{orderId}/status")
public ResponseEntity<?> updateStatus(
        @PathVariable Long orderId,
        @RequestParam String status) {

    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus(status);
    orderRepository.save(order);

    return ResponseEntity.ok("Status updated to: " + status);
}

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        orderRepository.deleteById(orderId);
        return ResponseEntity.ok("Order deleted");
    }
}
