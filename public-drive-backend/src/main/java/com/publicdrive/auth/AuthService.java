package com.publicdrive.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.publicdrive.Entity.User;
import com.publicdrive.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenUtil.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }
    
    @Transactional
    public boolean registerUser(String username, String password, String email) {
        // Check if username already exists
        if (userRepository.findByUsername(username) != null) {
            return false;  // Username already taken
        }

        // Check if email already exists
        if (userRepository.findByEmail(email) != null) {
            return false;  // Email already taken
        }
        String hashedPassword = passwordEncoder.encode(password);

        // Create new user entity
        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);

        // Save user to the database
        userRepository.save(user);

        return true; // Registration successful
    }
}