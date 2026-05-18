package com.personal.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.project.dto.LoginRequest;
import com.personal.project.mapper.AddressMapper;
import com.personal.project.mapper.VendorBranchMapper;
import com.personal.project.mapper.VendorMapper;
import com.personal.project.security.jwt.JwtService;
import com.personal.project.security.service.CustomUserDetailsService;
import com.personal.project.security.service.VendorUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.personal.project.repository.VendorRepository;
import com.personal.project.repository.VendorBranchRepository;
import com.personal.project.repository.AddressRepository;
import com.personal.project.service.GoldPriceService;
import com.personal.project.entity.Vendor;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.nullValue;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VendorAuthController.class)
@AutoConfigureMockMvc(
        addFilters = false
)
class VendorAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private VendorUserDetailsService vendorUserDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private VendorRepository vendorRepository;

    @MockitoBean
    private VendorBranchRepository vendorBranchRepository;

    @MockitoBean
    private AddressRepository addressRepository;

    @MockitoBean
    private GoldPriceService goldPriceService;

    @MockitoBean
    private VendorMapper vendorMapper;

    @MockitoBean
    private AddressMapper addressMapper;

    @MockitoBean
    private VendorBranchMapper vendorBranchMapper;

    @Test
    void shouldLoginVendorSuccessfully()
            throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "vendor@gmail.com"
        );

        request.setPassword(
                "password123"
        );

        UserDetails userDetails =
                User.builder()
                        .username(
                                "vendor@gmail.com"
                        )
                        .password(
                                "encodedPassword"
                        )
                        .roles(
                                "VENDOR"
                        )
                        .build();

        when(
                vendorUserDetailsService.loadUserByUsername(
                        "vendor@gmail.com"
                )
        ).thenReturn(
                userDetails
        );

        Vendor mockVendor = new Vendor();
        mockVendor.setVendorName("Test Vendor");
        mockVendor.setContactEmail("vendor@gmail.com");
        mockVendor.setVendorId(1);

        when(vendorRepository.findByContactEmail("vendor@gmail.com")).thenReturn(Optional.of(mockVendor));

        when(
                passwordEncoder.matches(
                        "password123",
                        "encodedPassword"
                )
        ).thenReturn(
                true
        );

        when(
                jwtService.generateToken(
                        userDetails
                )
        ).thenReturn(
                "mocked-vendor-jwt-token"
        );

        mockMvc.perform(
                        post("/vendor/auth/login")
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
                                        "mocked-vendor-jwt-token"
                                )
                )
                .andExpect(jsonPath("$.vendor_id").value(1))
                .andExpect(jsonPath("$.user_id").value(nullValue()));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidVendorCredentials()
            throws Exception {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "vendor@gmail.com"
        );

        request.setPassword(
                "wrong-password"
        );

        when(
                vendorUserDetailsService.loadUserByUsername(
                        "vendor@gmail.com"
                )
        ).thenThrow(
                new BadCredentialsException(
                        "Bad credentials"
                )
        );

        mockMvc.perform(
                        post("/vendor/auth/login")
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
                        post("/vendor/auth/login")
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
                        post("/vendor/auth/login")
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
                "vendor@gmail.com"
        );

        request.setPassword(
                ""
        );

        mockMvc.perform(
                        post("/vendor/auth/login")
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
