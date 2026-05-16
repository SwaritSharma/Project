package com.personal.project.security.filter;

import com.personal.project.security.jwt.JwtService;
import com.personal.project.security.service.CustomUserDetailsService;
import com.personal.project.security.service.VendorUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final
    JwtService
            jwtService;

    private final
    CustomUserDetailsService
            userDetailsService;

    private final
    VendorUserDetailsService
            vendorUserDetailsService;

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request
    ) {

        String path =
                request.getServletPath();

        return path.startsWith(
                "/user/auth"
        ) || path.startsWith(
                "/vendor/auth"
        );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader =
                request.getHeader(
                        "Authorization"
                );

        final String jwt;

        final String email;

        if (
                authHeader == null
                        || !authHeader.startsWith(
                        "Bearer "
                )
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        jwt =
                authHeader.substring(7);

        email =
                jwtService.extractUsername(jwt);

        if (
                email != null
                        &&
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                == null
        ) {

            UserDetails userDetails;

            try {

                userDetails =
                        userDetailsService
                                .loadUserByUsername(
                                        email
                                );

            } catch (
                    UsernameNotFoundException ex
            ) {

                userDetails =
                        vendorUserDetailsService
                                .loadUserByUsername(
                                        email
                                );
            }

            if (
                    jwtService.isTokenValid(
                            jwt,
                            userDetails
                    )
            ) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authToken
                        );
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}