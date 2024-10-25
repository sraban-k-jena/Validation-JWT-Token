package com.jt.jwt_token_validation.service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    public String secretKey;

    @Value("${jwt.expiration}")
    public long jwtExpirational;

    public Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirational))
                .signWith(key)
                .compact();

    }

    // Extract Username for Token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Extract Claims For token .
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate Token
    public boolean validateToken(String token, String username) {
        String extractUsername = extractUsername(token);
        return (extractUsername.equals(username) && !isTokenExpiredToken(token));
    }

    private boolean isTokenExpiredToken(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
