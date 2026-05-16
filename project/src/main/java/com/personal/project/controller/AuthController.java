package com.personal.project.controller;

import com.personal.project.dto.LoginRequest;
import com.personal.project.dto.LoginResponse;
import com.personal.project.exception.InvalidCredentialsException;
import com.personal.project.security.jwt.JwtService;
import com.personal.project.security.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final
    AuthenticationManager
            authenticationManager;

    private final
    CustomUserDetailsService
            userDetailsService;

    private final
    JwtService
            jwtService;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (
                BadCredentialsException ex
        ) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        UserDetails userDetails =
                userDetailsService
                        .loadUserByUsername(
                                request.getEmail()
                        );

        String token =
                jwtService.generateToken(
                        userDetails
                );

        return new LoginResponse(
                token
        );
    }
}