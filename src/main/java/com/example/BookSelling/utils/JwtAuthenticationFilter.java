package com.example.BookSelling.utils;

import com.example.BookSelling.repository.InvalidatedTokenRepository;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.text.ParseException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get Authorization Header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // get token from header
        String token = authHeader.substring(7);
        log.info("Extracted Token: {}", token);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            if (jti == null) {
                filterChain.doFilter(request, response);
                return;
            }

            var expireToken = invalidatedTokenRepository.findByToken(jti);
            if (expireToken.isPresent()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\": 401, \"message\": \"Unauthenticated\"}");
                response.getWriter().flush();
                response.getWriter().close();
            }

        } catch (ParseException e) {
            log.error("Error parsing JWT: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token format");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }

        // continue execute request if valid
        filterChain.doFilter(request, response);
    }
}
