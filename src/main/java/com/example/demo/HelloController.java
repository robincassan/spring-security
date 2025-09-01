package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Endpoint public : http://localhost:8080/hello/public
    @GetMapping("/hello/public")
    public String hellopublic() {
        return "Hello public";
    }

    // Endpoint private : http://localhost:8080/hello/private
    @GetMapping("/hello/private")
    public String helloprivate() {
        return "hello private";
    }

    // Endpoint public POST
    @PostMapping("/hello/public")
    public String helloPublicPost() {
        return "Hello public (POST)";
    }
}

