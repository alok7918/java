package com.example.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.PaymentRequest;
import com.example.dto.PaymentVerificationRequest;
import com.example.repository.CartItemRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Autowired
    OrderController orderController;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest paymentRequest) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            // Create the order in Razorpay
            JSONObject options = new JSONObject();
            options.put("amount", paymentRequest.getAmount() * 100);
            options.put("currency", paymentRequest.getCurrency());
            options.put("receipt", "order_rcptid_" + System.currentTimeMillis());

            Order order = client.orders.create(options);

            String orderId = order.get("id");


            return ResponseEntity.ok(Map.of(
                    "orderId", orderId,
                    "razorpayKey", razorpayKeyId
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating Razorpay order");
        }
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        try {

            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());

            String username = request.getUsername();

            Utils.verifyPaymentSignature(attributes, razorpayKeySecret);


            orderController.placeOrder(username, request.getRazorpayPaymentId());
            return ResponseEntity.ok("Payment verified successfully!");

        } catch (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid payment signature");
        }
    }
}
