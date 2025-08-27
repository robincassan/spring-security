package com.example.tp1c;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-app")
public class UserAppController {

    @Autowired
    private UserAppRepository userAppRepository;

    @GetMapping()
    public String userApp() throws Exception{
        return userAppRepository.findAll().toString();
    }
}
