package com.example.dto;

//PaymentRequest.java


public class PaymentRequest {
 private int amount;
 private String currency;
 private String username;

 // Getters & setters
 public int getAmount() {
     return amount;
 }
 public void setAmount(int amount) {
     this.amount = amount;
 }
 public String getCurrency() {
     return currency;
 }
 public void setCurrency(String currency) {
     this.currency = currency;
 }
 public String getUsername() {
     return username;
 }
 public void setUsername(String username) {
     this.username = username;
 }
}
