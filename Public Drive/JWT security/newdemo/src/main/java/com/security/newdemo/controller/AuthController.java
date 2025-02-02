package com.security.newdemo.controller;

import java.util.concurrent.ConcurrentHashMap;

import com.security.newdemo.entity.User;
import com.security.newdemo.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.security.newdemo.entity.AuthResponse;
import com.security.newdemo.entity.LoginRequest;
import com.security.newdemo.filter.CustomJWTTokenService;
@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RegisterService userService;

    @Autowired
    private CustomJWTTokenService tokenService;

    // Store active tokens per username
    private final ConcurrentHashMap<String, String> activeTokens = new ConcurrentHashMap<>();


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);

        String token = tokenService.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok("user added successfully");
    }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    //     String username = loginRequest.getUsername();

    //     // Check if there is an active token for this username
    //     if (activeTokens.containsKey(username)) {
    //         String existingToken = activeTokens.get(username);
    //         // Validate existing token
    //         if (tokenService.validateToken(existingToken)) {
    //             return ResponseEntity.badRequest().body("Only one active session is allowed for this user.");
    //         }
    //     }

    //     // Authenticate user credentials
    //     UsernamePasswordAuthenticationToken authToken =
    //             new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword());
    //     Authentication authentication = authenticationManager.authenticate(authToken);

    //     // Generate new token and store it
    //     String newToken = tokenService.generateToken(username);
    //     activeTokens.put(username, newToken);

    //     return ResponseEntity.ok(new AuthResponse(newToken));
    // }

     // Optional: Method to clear token on logout
     @PostMapping("/logout")
     public ResponseEntity<?> logout(@RequestBody LoginRequest loginRequest) {
         activeTokens.remove(loginRequest.getUsername());
         return ResponseEntity.ok("Logged out successfully.");
     }
}

