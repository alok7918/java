package com.example.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> otpStorage = new HashMap<>();

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "❌ User not found with that email!";
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(email, otp); // store OTP in-memory

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for Password Reset");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);

        return "✅ OTP sent to your email!";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "❌ User not found!";
        }

        String validOtp = otpStorage.get(email);
        if (validOtp == null || !validOtp.equals(otp)) {
            return "❌ Invalid OTP!";
        }

        // Update password
        User user = optionalUser.get();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepo.save(user);

        // Remove OTP from storage
        otpStorage.remove(email);

        return "✅ Password updated successfully!";
    }
}
