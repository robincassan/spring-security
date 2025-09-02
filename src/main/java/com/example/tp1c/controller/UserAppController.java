package com.example.tp1c.controller;

import com.example.tp1c.service.CustomUserDetailsService;
import com.example.tp1c.UserApp;
import com.example.tp1c.repository.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-app")
public class UserAppController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserAppRepository userAppRepository;

    @GetMapping
    public List<UserApp> getUsers() {
        return userAppRepository.findAll();
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserApp userApp) throws Exception {
        customUserDetailsService.createUser(
                userApp.getEmail(),
                userApp.getPassword()
        );
        return "utilisateur crée";
    }
}
