package com.example.TP2C.controller;

import com.example.TP2C.model.UserApp;
import com.example.TP2C.repository.UserAppRepository;
import com.example.TP2C.service.JwtAuthentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserAppRepository userRepository;

    @Autowired
    private JwtAuthentificationService jwtService;

    // 1️⃣ Créer un utilisateur
    @PostMapping("/register")
    public UserApp register(@RequestBody UserApp user) {
        return userRepository.save(user);
    }

    // 2️⃣ Login : vérifier username + password et renvoyer un JWT
    @PostMapping("/login")
    public String login(@RequestBody UserApp user) {
        Optional<UserApp> dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser.isPresent() && dbUser.get().getPassword().equals(user.getPassword())) {
            return jwtService.generateToken(user.getUsername());
        }
        return "Invalid username/password";
    }

    // 3️⃣ Vérifier un JWT
    @GetMapping("/verify-jwt/{jwt}")
    public boolean verifyJwt(@PathVariable String jwt) {
        return jwtService.verifyToken(jwt);
    }
}
