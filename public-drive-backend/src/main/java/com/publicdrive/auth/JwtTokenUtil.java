package com.publicdrive.auth;

import io.jsonwebtoken.*;

import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private String secretKey = "your-secret-key";  // Use a strong secret key in production

    // Method to generate JWT token from a username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Method to create the JWT token with specific claims and username
    private String createToken(Map<String, Object> claims, String username) {
        long expirationTime = 1000 * 60 * 60; // 1 hour

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)  // Set the username as the subject of the JWT
                .setIssuedAt(new Date())  // Set the issued at time
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))   // Use HMAC (HS256) algorithm and sign with the secret key
                .compact();  // Build and return the JWT token
    }

    // Extract the username (subject) from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic method to extract claims from the JWT token
    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // Extract all claims from the token
        return claimsResolver.resolve(claims);  // Resolve the required claim (e.g., username or expiration)
    }

    // Method to extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  // Set the signing key to validate the token signature
                .build()
                .parseClaimsJws(token)  // Parse the JWT token and get all claims
                .getBody();
    }

    // Check if the JWT token has expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // Compare expiration date with current time
    }

    // Validate the JWT token (check if the token is valid and not expired)
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));  // Validate token based on username and expiration
    }

    // Interface for resolving claims from the JWT
    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);  // Resolve the claims from the JWT token
    }
}
