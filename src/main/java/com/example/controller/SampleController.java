package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/admin")
public class SampleController {
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Welcome Admin";
    }
}

@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping("/dashboard")
    public String userDashboard() {
        return "Welcome User";
    }
}