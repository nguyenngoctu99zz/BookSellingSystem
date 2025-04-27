package com.example.BookSelling.service.impl;

import com.example.BookSelling.common.UserRole;
import com.example.BookSelling.dto.request.AuthenticationRequest;
import com.example.BookSelling.dto.request.LogoutRequest;
import com.example.BookSelling.dto.request.RefreshRequest;
import com.example.BookSelling.dto.response.AuthenticationResponse;
import com.example.BookSelling.dto.response.LogoutResponse;
import com.example.BookSelling.dto.response.RefreshResponse;
import com.example.BookSelling.exception.AppException;
import com.example.BookSelling.exception.ErrorCode;
import com.example.BookSelling.model.InvalidatedToken;
import com.example.BookSelling.model.User;
import com.example.BookSelling.repository.InvalidatedTokenRepository;
import com.example.BookSelling.repository.UserRepository;
import com.example.BookSelling.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected long REFRESH_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (!user.isActive()) {
            throw new AppException(ErrorCode.USER_INACTIVE);
        }
        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }
    public String generateAccessToken(User user) {
        return generateToken(user, VALID_DURATION, "ACCESS");
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, REFRESH_DURATION, "REFRESH");
    }
    public String generateToken(User user, long durationSeconds, String tokenType) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet =new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("My domain") //website's domain
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(durationSeconds, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("type", tokenType)
                .claim("roles", buildRole(user))
                .claim("userId", user.getUserId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }
    private String buildRole(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        UserRole userRole = user.getUserRole();
        if(userRole != null) {
            stringJoiner.add(user.getUserRole().toString());
        }
        return "ROLE_" + stringJoiner;
    }
    @Override
    public LogoutResponse logout(LogoutRequest request) throws ParseException, JOSEException {
        var signedToken = verifyToken(request.getToken());
        String jti = signedToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedToken.getJWTClaimsSet().getExpirationTime();
        String username = signedToken.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(jti)
                .expiryTime(expirationTime)
                .user(user)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        return LogoutResponse.builder().logout(true).build();
    }
    @Override
    public RefreshResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws ParseException, JOSEException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var signJWT = verifyToken(refreshToken);
        String tokenType = signJWT.getJWTClaimsSet().getStringClaim("type");
        if (!"REFRESH".equals(tokenType)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String jti = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        String username = signJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(jti)
                .expiryTime(expiryTime)
                .user(user)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);

        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(REFRESH_DURATION)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return RefreshResponse.builder()
                .newAccessToken(newAccessToken)
                .newRefreshToken(newRefreshToken)
                .authenticated(true)
                .build();
    }

    /// Tú: tôi sửa từ private sang public
    public SignedJWT verifyToken(String token)
            throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = new Date(signedJWT
                                .getJWTClaimsSet()
                                .getIssueTime()
                                .toInstant()
                                .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                                .toEpochMilli());
        var verified = signedJWT.verify(verifier);
        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsByToken(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }



}
