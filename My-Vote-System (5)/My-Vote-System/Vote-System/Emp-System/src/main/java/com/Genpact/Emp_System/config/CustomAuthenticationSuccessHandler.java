package com.Genpact.Emp_System.config;

import com.Genpact.Emp_System.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .filter(authority -> authority instanceof SimpleGrantedAuthority)
                .map(authority -> (SimpleGrantedAuthority) authority)
                .collect(Collectors.toList());

        if (authorities.stream().anyMatch(role -> role.getAuthority().equals("USER"))) {
            response.sendRedirect("/candidates/index");
        } else {
            response.sendRedirect("/candidates/adminpage");
        }
    }
}
