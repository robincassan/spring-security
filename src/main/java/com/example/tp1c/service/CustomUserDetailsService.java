package com.example.tp1c.service;

import com.example.tp1c.UserApp;
import com.example.tp1c.repository.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserAppRepository repo;
    @Autowired
    private PasswordEncoder bcrypt;

    public void createUser(String username, String password) {
        repo.save(
                new UserApp(
                        username,
                        bcrypt.encode(password)
                )
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException{
        var user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }


}
