package com.example.BookSelling.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getCurrentLogin() {
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(contextHolder.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) return null;

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaim("userId");
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
}