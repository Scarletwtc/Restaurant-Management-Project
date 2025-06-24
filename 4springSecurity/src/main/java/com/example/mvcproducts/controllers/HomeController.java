package com.example.mvcproducts.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication auth) {
        for (GrantedAuthority authority : auth.getAuthorities()) {
            switch(authority.getAuthority()) {
                case "ROLE_CHEF":
                    return "redirect:/chefboard";
                case "ROLE_SUPPLIER":
                    return "redirect:/supplierboard";
                case "ROLE_OWNER":
                    return "redirect:/dashboard";
            }
        }
        // fallback
        return "home";
    }

    @GetMapping("/chefboard")
    public String chefBoard() {
        return "chefboard";
    }

    @GetMapping("/supplierboard")
    public String supplierBoard() {
        return "supplierboard";
    }
}
