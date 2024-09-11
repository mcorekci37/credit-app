package com.emce.authserver.controller;

import com.emce.authserver.dto.AuthRequest;
import com.emce.authserver.dto.AuthResponse;
import com.emce.authserver.dto.RegisterRequest;
import com.emce.authserver.service.AuthService;
import com.emce.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest registerRequest){
        return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }

    @GetMapping("/checkExists/{userId}")
    public ResponseEntity<Boolean> checkExists(@PathVariable("userId") Integer userId){
        boolean userCredential = userService.checkExistsById(userId);
        return ResponseEntity.ok(userCredential);
    }
    @PutMapping("/update")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody RegisterRequest registerRequest){
        return new ResponseEntity<>(authService.update(registerRequest), HttpStatus.OK);
    }
}
