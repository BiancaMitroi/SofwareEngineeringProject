package com.example.app.controller;

import com.example.app.models.User;
import com.example.app.models.dto.LoginRequest;
import com.example.app.repo.UserRepo;
import com.example.app.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication); // seteaza autentificarea in contextul de spring, sa fie available la nivel de app
        String jwtToken = jwtUtils.generateJwtToken(authentication); // genereaza jwttoken pentru user

        return ResponseEntity.ok(jwtToken); // returneaza token
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody LoginRequest loginRequest) {
        User user = new User(0L, loginRequest.getUsername(), passwordEncoder.encode(loginRequest.getPassword()), "USER");
        if(userRepo.findByUsername(loginRequest.getUsername()).isEmpty()) { // daca nu exista deja user-ul
            userRepo.save(user); // il salvam
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // altfel bad request
        }
    }
}
