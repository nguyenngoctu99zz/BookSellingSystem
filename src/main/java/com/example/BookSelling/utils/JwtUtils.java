package com.example.BookSelling.utils;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.signer-key}")
    private String signerKey;

    public boolean validateToken(String authToken) {
        try {
            JWSObject jwsObject = JWSObject.parse(authToken);
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
            return jwsObject.verify(verifier);
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            String username = jwsObject.getPayload().toJSONObject().get("sub").toString();

            return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.emptyList()
            );
        } catch (Exception e) {
            return null;
        }
    }
}