package com.Genpact.Vote_System.service;

import com.Genpact.Vote_System.dto.UserProfileDto;
import com.Genpact.Vote_System.dto.UserRegisterDto;
import com.Genpact.Vote_System.entity.User;
import com.Genpact.Vote_System.entity.UserRole;
import com.Genpact.Vote_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_CODE = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Override
    public User save(UserRegisterDto userRegDto) {
        User user = new User();
        user.setAadharNumber(userRegDto.getAadharNumber());
        user.setPassword(passwordEncoder.encode(userRegDto.getPassword()));

        UserRole role;
        try {

            role = UserRole.valueOf(userRegDto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + userRegDto.getRole());
        }
        user.setRole(role);
        user.setFirstName(userRegDto.getFirstName());
        user.setLastName(userRegDto.getLastName());
        user.setPhoneNumber(userRegDto.getPhoneNumber());
        user.setDateOfBirth(userRegDto.getDateOfBirth());
        user.setNationality(userRegDto.getNationality());
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (ADMIN_CODE.equals(username)) {
            if (passwordEncoder.matches(ADMIN_PASSWORD, ADMIN_PASSWORD)) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
                return new org.springframework.security.core.userdetails.User(username, ADMIN_PASSWORD, authorities);
            } else {
                throw new UsernameNotFoundException("Invalid admin password");
            }
        }

        User user = userRepository.findByAadharNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Aadhaar number: " + username));


        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getAadharNumber(), user.getPassword(), authorities);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public void updateProfile(Long userId, UserProfileDto userProfileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setPhoneNumber(userProfileDto.getPhoneNumber());

        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByAadharNumber(String aadharNumber) {
        return userRepository.findByAadharNumber(aadharNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User with Aadhaar number " + aadharNumber + " not found"));
    }

    public void voteForCandidate(Long candidateId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        user.incrementVoteCount();


        userRepository.save(user);
    }

    public void incrementVoteCount(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setVotecount(user.getVotecount() + 1);
        userRepository.save(user);
    }



}
