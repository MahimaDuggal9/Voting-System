package com.Genpact.Vote_System.controller;

import com.Genpact.Vote_System.entity.Candidate;
import com.Genpact.Vote_System.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.Genpact.Vote_System.dto.UserRegisterDto;
import com.Genpact.Vote_System.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegisterDto userRegistrationDto() {
        return new UserRegisterDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegisterDto registrationDto, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        try {
            userService.save(registrationDto);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("phone_number")) {
                logger.warn("Duplicate phone number attempt: " + registrationDto.getPhoneNumber());
                result.rejectValue("phoneNumber", "error.user", "Phone number must be unique. Please enter a different one.");
            } else if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("aadhar_number")) {
                logger.warn("Duplicate Aadhaar number attempt: " + registrationDto.getAadharNumber());
                result.rejectValue("aadharNumber", "error.user", "Aadhaar number must be unique. Please enter a different one.");
            }
            return "registration"; // Return to the registration page with the error
        } catch (Exception e) {
            logger.error("Error registering user", e);
            return "error"; // Handle other errors appropriately
        }

        return "redirect:/login"; // Redirect to login page if registration is successful
    }

}
