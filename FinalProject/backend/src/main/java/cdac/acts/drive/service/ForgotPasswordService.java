package cdac.acts.drive.service;

import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Value("${jwt.secret}")  // Store in application.properties
    private String jwtSecret;

    public ForgotPasswordService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public String generateResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Generate JWT Token
            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 minutes expiry
                    .signWith(SignatureAlgorithm.HS256, jwtSecret)
                    .compact();

            // Save token & expiry in database
            user.setResetToken(token);
            user.setResetTokenExpiry(new Date(System.currentTimeMillis() + 30 * 60 * 1000));
            userRepository.save(user);

            // Send email
            sendResetEmail(user.getEmail(), token);
            return "Password reset link sent successfully.";
        } else {
            throw new RuntimeException("Email not found");
        }
    }

    private void sendResetEmail(String email, String token) {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + resetLink);

        mailSender.send(message);
    }
}
