package com.Genpact.Vote_System.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.Genpact.Vote_System.dto.UserRegisterDto;
import com.Genpact.Vote_System.service.UserService;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            return "registration";
        } catch (Exception e) {
            logger.error("Error registering user", e);
            return "error";
        }

        return "redirect:/login";
    }


}
