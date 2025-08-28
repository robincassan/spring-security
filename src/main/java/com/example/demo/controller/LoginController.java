package com.example.demo.controller;

import com.example.demo.models.UserApp;
import com.example.demo.repositories.UserAppRepository;
import com.example.demo.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserAppRepository userAppRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserApp userApp) throws Exception {
        Optional<UserApp> userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional.isPresent() &&
                passwordEncoder.matches(userApp.getPassword(), userAppOptional.get().getPassword())) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtService.createAuthenticationToken(userAppOptional.get()).toString())
                    .body("connected");
        }
        throw new Exception();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserApp userApp) {
        Optional<UserApp> userAppOptional = userAppRepository.findByUsername(userApp.getUsername());
        if (userAppOptional.isEmpty()) {

            userAppRepository.save(
                    new UserApp(
                            userApp.getUsername(),
                            passwordEncoder.encode(userApp.getPassword()),
                            userApp.getRole() == null ? "USER" : userApp.getRole()
                    )
            );
            return ResponseEntity.ok("User registered");
        }else{
            return ResponseEntity.status(400).body("User already exists");
        }
    }
}
