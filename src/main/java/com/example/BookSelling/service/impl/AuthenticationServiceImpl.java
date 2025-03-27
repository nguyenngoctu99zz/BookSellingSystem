package com.example.BookSelling.service.impl;

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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .userId(user.getUserId())
                .token(token)
                .authenticated(true)
                .build();
    }

    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet =new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("My domain") //website's domain
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(VALID_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("ROLE", user.getUserRole())
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
    public RefreshResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken());
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

        var token = generateToken(user);
        return RefreshResponse.builder()
                .newToken(token)
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token)
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
