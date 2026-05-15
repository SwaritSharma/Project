package com.personal.project.controllers;

import com.personal.project.dtos.UserDashboardResponseDto;
import com.personal.project.dtos.UserLoginRequestDto;
import com.personal.project.dtos.UserRegistrationRequestDto;
import com.personal.project.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDashboardResponseDto>
    registerUser(
            @Valid
            @RequestBody
            UserRegistrationRequestDto dto
    ) {

        UserDashboardResponseDto response =
                userService.registerUser(dto);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserDashboardResponseDto>
    loginUser(
            @Valid
            @RequestBody
            UserLoginRequestDto dto
    ) {

        UserDashboardResponseDto response =
                userService.authenticateUser(dto);

        return ResponseEntity.ok(
                response
        );
    }

    @GetMapping("/{userId}/dashboard")
    public ResponseEntity<UserDashboardResponseDto>
    getUserDashboard(
            @PathVariable
            Integer userId
    ) {

        UserDashboardResponseDto response =
                userService.getUserDashboard(userId);

        return ResponseEntity.ok(
                response
        );
    }
}