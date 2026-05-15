package com.personal.project.servicesImpl;

import com.personal.project.dtos.LatestTransactionDto;
import com.personal.project.dtos.UserDashboardResponseDto;
import com.personal.project.dtos.UserLoginRequestDto;
import com.personal.project.dtos.UserRegistrationRequestDto;
import com.personal.project.entity.TransactionHistory;
import com.personal.project.entity.User;
import com.personal.project.exceptions.DuplicateResourceException;
import com.personal.project.exceptions.InvalidCredentialsException;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.UserMapper;
import com.personal.project.repositories.UserRepository;
import com.personal.project.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDashboardResponseDto registerUser(
            UserRegistrationRequestDto dto
    ) {

        String email =
                dto.getEmail()
                        .trim()
                        .toLowerCase();

        boolean exists =
                userRepository.existsByEmail(
                        email
                );

        if (exists) {
            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }

        User user =
                userMapper.toUser(dto);

        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        user.setBalance(
                BigDecimal.ZERO
        );

        User savedUser =
                userRepository.save(user);

        List<TransactionHistory> transactions =
                userRepository
                        .findTop5ByUserUserIdOrderByCreatedAtDesc(
                                savedUser.getUserId()
                        );

        List<LatestTransactionDto> latestTransactions =
                userMapper.toLatestTransactionDtos(
                        transactions
                );

        UserDashboardResponseDto response =
                userMapper.toUserDashboardResponseDto(
                        savedUser
                );

        response.setLatestTransactions(
                latestTransactions
        );

        return response;
    }

    @Override
    public UserDashboardResponseDto authenticateUser(
            UserLoginRequestDto dto
    ) {

        String email =
                dto.getEmail()
                        .trim()
                        .toLowerCase();

        User user =
                userRepository.findByEmail(
                                email
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        boolean matches =
                passwordEncoder.matches(
                        dto.getPassword(),
                        user.getPassword()
                );

        if (!matches) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        List<TransactionHistory> transactions =
                userRepository
                        .findTop5ByUserUserIdOrderByCreatedAtDesc(
                                user.getUserId()
                        );

        List<LatestTransactionDto> latestTransactions =
                userMapper.toLatestTransactionDtos(
                        transactions
                );

        UserDashboardResponseDto response =
                userMapper.toUserDashboardResponseDto(
                        user
                );

        response.setLatestTransactions(
                latestTransactions
        );

        return response;
    }

    @Override
    public UserDashboardResponseDto getUserDashboard(
            Integer userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        List<TransactionHistory> transactions =
                userRepository
                        .findTop5ByUserUserIdOrderByCreatedAtDesc(
                                userId
                        );

        List<LatestTransactionDto> latestTransactions =
                userMapper.toLatestTransactionDtos(
                        transactions
                );

        UserDashboardResponseDto response =
                userMapper.toUserDashboardResponseDto(
                        user
                );

        response.setLatestTransactions(
                latestTransactions
        );

        return response;
    }
}