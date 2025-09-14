package com.example.dto;

//PaymentVerificationRequest.java


public class PaymentVerificationRequest {
 private String razorpayPaymentId;
 private String razorpayOrderId;
 private String razorpaySignature;
 private String username;

 // Getters & setters
 public String getRazorpayPaymentId() {
     return razorpayPaymentId;
 }
 public void setRazorpayPaymentId(String razorpayPaymentId) {
     this.razorpayPaymentId = razorpayPaymentId;
 }
 public String getRazorpayOrderId() {
     return razorpayOrderId;
 }
 public void setRazorpayOrderId(String razorpayOrderId) {
     this.razorpayOrderId = razorpayOrderId;
 }
 public String getRazorpaySignature() {
     return razorpaySignature;
 }
 public void setRazorpaySignature(String razorpaySignature) {
     this.razorpaySignature = razorpaySignature;
 }
 public String getUsername() {
     return username;
 }
 public void setUsername(String username) {
     this.username = username;
 }
}
