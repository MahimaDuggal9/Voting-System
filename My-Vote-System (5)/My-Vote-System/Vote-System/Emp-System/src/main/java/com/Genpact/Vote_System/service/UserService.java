package com.Genpact.Vote_System.service;

import com.Genpact.Vote_System.dto.UserRegisterDto;
import com.Genpact.Vote_System.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(UserRegisterDto userRegDto);
}
