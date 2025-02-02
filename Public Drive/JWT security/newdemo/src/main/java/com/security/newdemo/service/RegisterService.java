package com.security.newdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.newdemo.dao.UserRepository;
import com.security.newdemo.entity.User;

@Service
public class RegisterService {
    
    @Autowired
    private UserRepository userRepository; 
    @Autowired
    private PasswordEncoder passwordEncoder;

    // custom method : saving user
    public void saveUser(User user) {
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole()==null){
            user.setRole("ROLE_USER");
        }
        userRepository.save(user);
    }
}
