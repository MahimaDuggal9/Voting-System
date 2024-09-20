package com.Genpact.Emp_System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.Genpact.Emp_System.dto.UserRegisterDto;
import com.Genpact.Emp_System.service.UserService;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

        if (!isAgeValid(registrationDto.getDateOfBirth())) {
            result.rejectValue("dateOfBirth", "error.user", "You must be at least 18 years old to register.");
            return "registration";
        }

        try {
            userService.save(registrationDto);
        } catch (Exception e) {
            logger.error("Error registering user", e);
            return "error";
        }

        return "redirect:/login";
    }

    private boolean isAgeValid(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) return false;

        try {
            LocalDate dob = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();
            int age = Period.between(dob, today).getYears();
            return age >= 18;
        } catch (DateTimeParseException e) {
            return false; // Invalid date format
        }
    }
}
