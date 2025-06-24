package com.example.mvcproducts.controllers;

import com.example.mvcproducts.domain.User;
import com.example.mvcproducts.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    
    private final UserService userService;
    
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }
    
    @PostMapping("/register/chef")
    public String registerChef(@RequestParam String username, @RequestParam String password) {
        try {
            userService.registerChef(username, password);
            return "redirect:/login?registered";
        } catch (Exception e) {
            return "redirect:/register?error";
        }
    }
    
    @PostMapping("/register/supplier")
    public String registerSupplier(@RequestParam String username, @RequestParam String password) {
        try {
            userService.registerSupplier(username, password);
            return "redirect:/login?registered";
        } catch (Exception e) {
            return "redirect:/register?error";
        }
    }
    
    @PostMapping("/register/owner")
    @PreAuthorize("hasRole('OWNER')")
    public String registerOwner(@RequestParam String username, @RequestParam String password) {
        try {
            userService.registerOwner(username, password);
            return "redirect:/dashboard?ownerAdded";
        } catch (Exception e) {
            return "redirect:/dashboard?error";
        }
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
} 