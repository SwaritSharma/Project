package com.personal.project.controller;

import com.personal.project.dto.LoginRequest;
import com.personal.project.dto.LoginResponse;
import com.personal.project.exception.InvalidCredentialsException;
import com.personal.project.security.jwt.JwtService;
import com.personal.project.security.service.CustomUserDetailsService;
import com.personal.project.entity.User;
import com.personal.project.entity.Address;
import com.personal.project.repository.UserRepository;
import com.personal.project.repository.AddressRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private final
    UserRepository
            userRepository;

    private final
    AddressRepository
            addressRepository;

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

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        return new LoginResponse(
                token,
                user.getName(),
                user.getEmail(),
                "USER",
                user.getUserId()
        );
    }

    @PostMapping("/register")
    public LoginResponse register(
            @Valid @RequestBody com.personal.project.dto.RegisterRequest request
    ) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address = addressRepository.save(address);

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(BigDecimal.ZERO);
        user.setCreatedAt(LocalDateTime.now());
        user.setAddress(address);
        
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                user.getName(),
                user.getEmail(),
                "USER",
                user.getUserId()
        );
    }
}