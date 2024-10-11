package com.Genpact.Vote_System.controller;

import com.Genpact.Vote_System.entity.User;
import com.Genpact.Vote_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public String profile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String aadharNumber = authentication.getName();
            User user = userService.findByAadharNumber(aadharNumber);
            model.addAttribute("user", user);
        }
        return "profile";
    }

}
