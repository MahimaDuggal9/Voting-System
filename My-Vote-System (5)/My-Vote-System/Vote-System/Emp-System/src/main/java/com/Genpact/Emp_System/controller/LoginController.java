package com.Genpact.Emp_System.controller;

import com.Genpact.Emp_System.dto.LoginDto;
import com.Genpact.Emp_System.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate(LoginDto loginDto) {
        boolean isValidUser = loginService.validateUser(loginDto);
        if (isValidUser) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))) {
                    return "redirect:/candidates/adminpage";
                } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("USER"))) {
                    return "redirect:/candidates/index";
                }
            }
        }
        return "redirect:/login?error";
    }
}
