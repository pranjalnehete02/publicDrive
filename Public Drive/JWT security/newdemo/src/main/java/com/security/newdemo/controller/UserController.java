
package com.security.newdemo.controller;

import com.security.newdemo.entity.User;
import com.security.newdemo.service.RegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private RegisterService userService;

    @PostMapping("/add-user")
    public String addUser(@RequestBody User user) {
        userService.saveUser(user);
        return "User added successfully!";
    }
}
