package com.korit.perfume.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final JwtUtils jwtUtils;

    public JwtTokenProvider(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public String createToken(String username, String nickname) {
        return jwtUtils.generateAccessToken(username);
    }

    public Claims getClaims(String token) {
        return jwtUtils.getClaims(token);
    }
}
