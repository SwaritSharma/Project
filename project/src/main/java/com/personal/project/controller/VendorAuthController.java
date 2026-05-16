package com.personal.project.controller;

import com.personal.project.dto.LoginRequest;
import com.personal.project.dto.LoginResponse;
import com.personal.project.exception.InvalidCredentialsException;
import com.personal.project.security.jwt.JwtService;
import com.personal.project.security.service.VendorUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendor/auth")
@RequiredArgsConstructor
public class VendorAuthController {

    private final
    VendorUserDetailsService
            vendorUserDetailsService;

    private final
    JwtService
            jwtService;

    private final
    PasswordEncoder
            passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        try {

            DaoAuthenticationProvider authProvider =
                    new DaoAuthenticationProvider(
                            vendorUserDetailsService
                    );

            authProvider.setPasswordEncoder(
                    passwordEncoder
            );

            authProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (
                Exception ex
        ) {

            throw new InvalidCredentialsException(
                    "Invalid vendor credentials"
            );
        }

        UserDetails userDetails =
                vendorUserDetailsService
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