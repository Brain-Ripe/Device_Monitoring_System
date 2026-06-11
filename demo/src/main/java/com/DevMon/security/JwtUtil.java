package com.DevMon.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // In production, externalize this to application.properties / application.yml
    // This key must be at least 256 bits (32 bytes/characters) long for HS256 encryption.
    private final String SECRET_KEY = "your-super-secret-high-entropy-secure-key-for-devmon-project-2026";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 Hours in milliseconds

    // Converts our plaintext string key into a cryptographic type-safe SecretKey
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // 1. Generate a brand new token for a user who successfully logged in
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // 2. Extract the username (subject) directly from an incoming token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. Extract the expiration date to see if the token is still alive
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 4. Generic helper to extract ANY specific claim from the token payload
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 5. Parse, cryptographically verify, and unpack the token's JSON payload
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 6. Check if the token is completely valid (Username matches and it isn't expired)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 7. Check if the current system clock has passed the token's expiration timestamp
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}