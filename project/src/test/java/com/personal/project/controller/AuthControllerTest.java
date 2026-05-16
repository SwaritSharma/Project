package com.personal.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.project.dto.LoginRequest;
import com.personal.project.security.jwt.JwtService;
import com.personal.project.security.service.CustomUserDetailsService;
import com.personal.project.security.service.VendorUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(
        addFilters = false
)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private VendorUserDetailsService vendorUserDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldLoginSuccessfully()
            throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "test@gmail.com"
        );

        request.setPassword(
                "password123"
        );

        UserDetails userDetails =
                User.builder()
                        .username(
                                "test@gmail.com"
                        )
                        .password(
                                "encodedPassword"
                        )
                        .roles(
                                "USER"
                        )
                        .build();

        when(
                authenticationManager.authenticate(
                        any()
                )
        ).thenReturn(
                (Authentication) null
        );

        when(
                userDetailsService.loadUserByUsername(
                        "test@gmail.com"
                )
        ).thenReturn(
                userDetails
        );

        when(
                jwtService.generateToken(
                        userDetails
                )
        ).thenReturn(
                "mocked-jwt-token"
        );

        mockMvc.perform(
                        post("/user/auth/login")
                                .contentType(
                                        APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.token")
                                .value(
                                        "mocked-jwt-token"
                                )
                );
    }
    @Test
    void shouldReturnUnauthorizedForInvalidCredentials()
            throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "test@gmail.com"
        );

        request.setPassword(
                "wrong-password"
        );

        when(
                authenticationManager.authenticate(
                        any()
                )
        ).thenThrow(
                new BadCredentialsException(
                        "Bad credentials"
                )
        );

        mockMvc.perform(
                        post("/user/auth/login")
                                .contentType(
                                        APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }
    @Test
    void shouldReturnBadRequestForEmptyEmail()
            throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                ""
        );

        request.setPassword(
                "password123"
        );

        mockMvc.perform(
                        post("/user/auth/login")
                                .contentType(
                                        APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void shouldReturnBadRequestForInvalidEmailFormat()
            throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "invalid-email"
        );

        request.setPassword(
                "password123"
        );

        mockMvc.perform(
                        post("/user/auth/login")
                                .contentType(
                                        APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void shouldReturnBadRequestForEmptyPassword()
            throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "test@gmail.com"
        );

        request.setPassword(
                ""
        );

        mockMvc.perform(
                        post("/user/auth/login")
                                .contentType(
                                        APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isBadRequest()
                );
    }
}