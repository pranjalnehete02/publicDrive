//package cdac.acts.drive.filter;
//
//
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//@Service
//public class CustomJWTTokenService {
//
//    // 1. Creation of JWT token
//    private final SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiry
//                .signWith(jwtSecret) // Use the generated secret key for signing
//                .compact();
//    }
//
//    // 2. Validation of JWT token
//    public String validateToken(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(jwtSecret)
//                    .parseClaimsJws(token)
//                    .getBody();
//            return claims.getSubject(); // returns the username if valid
//        } catch (Exception e) {
//            return null; // Invalid token
//        }
//    }
//
//    // 3. Check if the token is expired
//    public boolean isTokenExpired(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(jwtSecret)
//                    .parseClaimsJws(token)
//                    .getBody();
//            return claims.getExpiration().before(new Date());
//        } catch (Exception e) {
//            return true; // Assume expired if there’s an exception
//        }
//    }
//}
package cdac.acts.drive.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class CustomJWTTokenService {

    @Value("${jwt.secret}")  // Get secret from application.properties
    private String jwtSecretString;

    private SecretKey jwtSecret; // Corrected type

    @PostConstruct
    public void init() {
        // Convert secret string to SecretKey
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecretString);
        jwtSecret = Keys.hmacShaKeyFor(keyBytes);
    }

    // 1. Generate JWT Token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiry
                .signWith(jwtSecret, SignatureAlgorithm.HS256) // ✅ Fixed signing issue
                .compact();
    }

    // 2. Validate JWT Token
    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Returns username if valid
        } catch (Exception e) {
            return null; // Invalid token
        }
    }

    // 3. Check if the Token is Expired
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // Assume expired if there's an exception
        }
    }
}
