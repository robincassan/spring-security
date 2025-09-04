package com.example.TP2C.controller;

import com.example.TP2C.model.UserApp;
import com.example.TP2C.service.JwtAuthentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtController {

    @Autowired
    private JwtAuthentificationService jwtService;

    // Endpoint POST /login qui reçoit un UserApp
    @PostMapping("/login")
    public String login(@RequestBody UserApp user) {
        return jwtService.generateToken(user.getUsername());
    }

    // Endpoint GET /get-jwt pour visualiser un JWT "test"
    @GetMapping("/get-jwt")
    public String getJwt() {
        return jwtService.generateToken("UtilisateurTest");
    }

    // Endpoint GET /create-jwt pour générer un JWT
    @GetMapping("/create-jwt")
    public String createJwt() {
        return jwtService.generateToken("UtilisateurTest");
    }
}
