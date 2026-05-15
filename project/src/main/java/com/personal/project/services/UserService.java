package com.personal.project.services;

import com.personal.project.dtos.UserDashboardResponseDto;
import com.personal.project.dtos.UserLoginRequestDto;
import com.personal.project.dtos.UserRegistrationRequestDto;

public interface UserService {

    UserDashboardResponseDto registerUser(
            UserRegistrationRequestDto dto
    );

    UserDashboardResponseDto authenticateUser(
            UserLoginRequestDto dto
    );

    UserDashboardResponseDto getUserDashboard(
            Integer userId
    );
}