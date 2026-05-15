package com.personal.project;

import com.personal.project.dtos.UserDashboardResponseDto;
import com.personal.project.dtos.UserLoginRequestDto;
import com.personal.project.dtos.UserRegistrationRequestDto;
import com.personal.project.entity.Address;
import com.personal.project.entity.User;
import com.personal.project.exceptions.DuplicateResourceException;
import com.personal.project.exceptions.InvalidCredentialsException;
import com.personal.project.exceptions.ResourceNotFoundException;
import com.personal.project.mappers.UserMapper;
import com.personal.project.repositories.UserRepository;
import com.personal.project.servicesImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private UserDashboardResponseDto
            userDashboardResponseDto;

    @BeforeEach
    void setUp() {

        Address address =
                Address.builder()
                        .street("Street 1")
                        .city("Mohali")
                        .state("Punjab")
                        .postalCode("160062")
                        .country("India")
                        .build();

        user = User.builder()
                .userId(1)
                .email("swarit@gmail.com")
                .name("Swarit Sharma")
                .password("encodedPassword")
                .balance(new BigDecimal("1000"))
                .address(address)
                .build();

        userDashboardResponseDto =
                UserDashboardResponseDto.builder()
                        .userId(1)
                        .name("Swarit Sharma")
                        .email("swarit@gmail.com")
                        .balance(
                                new BigDecimal("1000")
                        )
                        .street("Street 1")
                        .city("Mohali")
                        .state("Punjab")
                        .postalCode("160062")
                        .country("India")
                        .build();
    }

    @Test
    void registerUser_ShouldRegisterSuccessfully() {

        UserRegistrationRequestDto dto =
                UserRegistrationRequestDto.builder()
                        .name("Swarit Sharma")
                        .email("swarit@gmail.com")
                        .password("123456")
                        .street("Street 1")
                        .city("Mohali")
                        .state("Punjab")
                        .postalCode("160062")
                        .country("India")
                        .build();

        when(userRepository.existsByEmail(
                dto.getEmail()
        )).thenReturn(false);

        when(userMapper.toUser(dto))
                .thenReturn(user);

        when(passwordEncoder.encode(
                "123456"
        )).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        when(userMapper
                .toUserDashboardResponseDto(user))
                .thenReturn(
                        userDashboardResponseDto
                );

        UserDashboardResponseDto result =
                userService.registerUser(dto);

        assertNotNull(result);

        assertEquals(
                "Swarit Sharma",
                result.getName()
        );

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() {

        UserRegistrationRequestDto dto =
                UserRegistrationRequestDto.builder()
                        .email("swarit@gmail.com")
                        .build();

        when(userRepository.existsByEmail(
                dto.getEmail()
        )).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> userService.registerUser(dto)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void authenticateUser_ShouldLoginSuccessfully() {

        UserLoginRequestDto dto =
                UserLoginRequestDto.builder()
                        .email("swarit@gmail.com")
                        .password("123456")
                        .build();

        when(userRepository.findByEmail(
                dto.getEmail()
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedPassword"
        )).thenReturn(true);

        when(userMapper
                .toUserDashboardResponseDto(user))
                .thenReturn(
                        userDashboardResponseDto
                );

        UserDashboardResponseDto result =
                userService.authenticateUser(dto);

        assertNotNull(result);

        assertEquals(
                "Swarit Sharma",
                result.getName()
        );
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenEmailNotFound() {

        UserLoginRequestDto dto =
                UserLoginRequestDto.builder()
                        .email("wrong@gmail.com")
                        .password("123456")
                        .build();

        when(userRepository.findByEmail(
                dto.getEmail()
        )).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.authenticateUser(dto)
        );
    }

    @Test
    void authenticateUser_ShouldThrowException_WhenPasswordIncorrect() {

        UserLoginRequestDto dto =
                UserLoginRequestDto.builder()
                        .email("swarit@gmail.com")
                        .password("wrongPassword")
                        .build();

        when(userRepository.findByEmail(
                dto.getEmail()
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongPassword",
                "encodedPassword"
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> userService.authenticateUser(dto)
        );
    }

    @Test
    void getUserDashboard_ShouldReturnDashboardSuccessfully() {

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(userMapper
                .toUserDashboardResponseDto(user))
                .thenReturn(
                        userDashboardResponseDto
                );

        UserDashboardResponseDto result =
                userService.getUserDashboard(1);

        assertNotNull(result);

        assertEquals(
                "Swarit Sharma",
                result.getName()
        );
    }

    @Test
    void getUserDashboard_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserDashboard(1)
        );
    }
}