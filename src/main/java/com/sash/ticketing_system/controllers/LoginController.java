package com.sash.ticketing_system.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class LoginController {

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Handle login post request
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password) {
        return "redirect:/";
    }

}
