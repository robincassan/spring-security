package com.example.tp1B;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Endpoint public : http://localhost:8080/hello/public
    @GetMapping("/hello/public")
    public String helloHublic() {
        return "Hello public";
    }

    // Endpoint private : http://localhost:8080/hello/private
    @GetMapping("/hello/private")
    public String helloPrivate() {
        return "hello private";
    }

    @GetMapping("/hello/me")
    public String me(Authentication authentication) {
        return "Utilisateur connecté : " + authentication.getName() +
                " | Roles : " + authentication.getAuthorities();
    }



}

