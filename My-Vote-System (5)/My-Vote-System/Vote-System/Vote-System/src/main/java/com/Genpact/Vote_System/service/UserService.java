package com.Genpact.Vote_System.service;

import com.Genpact.Vote_System.dto.UserProfileDto;
import com.Genpact.Vote_System.dto.UserRegisterDto;
import com.Genpact.Vote_System.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    User save(UserRegisterDto userRegDto);
    void saveUser(User user);
    List<User> getAllUsers();
    User findByAadharNumber(String aadharNumber);
    void incrementVoteCount(Long userId);
    User findById(Long id);

}
