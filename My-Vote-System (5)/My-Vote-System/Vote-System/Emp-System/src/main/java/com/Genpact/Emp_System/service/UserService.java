package com.Genpact.Emp_System.service;

import com.Genpact.Emp_System.dto.UserRegisterDto;
import com.Genpact.Emp_System.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(UserRegisterDto userRegDto);
}
