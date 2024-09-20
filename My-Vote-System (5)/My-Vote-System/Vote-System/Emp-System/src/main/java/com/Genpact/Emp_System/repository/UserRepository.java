package com.Genpact.Emp_System.repository;

import com.Genpact.Emp_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByAadharNumber(String aadharNumber);


}
