package com.Genpact.Emp_System.service;

import com.Genpact.Emp_System.dto.LoginDto;
import com.Genpact.Emp_System.entity.User;
import com.Genpact.Emp_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public boolean validateUser(LoginDto loginDto) {
        User user = userRepository.findByAadharNumber(loginDto.getAadharNumber());
        if (user != null) {
            boolean isValidPassword = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
            boolean isValidRole = user.getRole().name().equalsIgnoreCase(loginDto.getRole());
            logger.info("Login attempt for user: {}", loginDto.getAadharNumber());
            return isValidPassword && isValidRole;
        } else {
            logger.warn("Login attempt failed: User not found for Aadhar number {}", loginDto.getAadharNumber());
            return false;
        }
    }
}
