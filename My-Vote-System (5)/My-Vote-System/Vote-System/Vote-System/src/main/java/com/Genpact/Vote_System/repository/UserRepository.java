package com.Genpact.Vote_System.repository;

import com.Genpact.Vote_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAadharNumber(String aadharNumber);

}
