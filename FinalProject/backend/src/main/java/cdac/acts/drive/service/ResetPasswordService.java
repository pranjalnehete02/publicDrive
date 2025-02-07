package cdac.acts.drive.service;

import cdac.acts.drive.entity.User;
import cdac.acts.drive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ResetPasswordService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    public String resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        System.out.println(userOptional.get());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if token is expired
            if (user.getResetTokenExpiry().before(new Date())) {
                return "Reset token has expired.";
            }

            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null); // Clear the reset token after use
            user.setResetTokenExpiry(null);
            System.out.println(user);
            userRepository.save(user);

            return "Password reset successful.";
        } else {
            return "Invalid reset token.";
        }
    }
}
