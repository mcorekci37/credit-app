package com.emce.authserver.service;

import com.emce.authserver.config.JwtUtil;
import com.emce.authserver.dto.AuthRequest;
import com.emce.authserver.dto.AuthResponse;
import com.emce.authserver.dto.RegisterRequest;
import com.emce.authserver.entity.Role;
import com.emce.authserver.entity.UserCredential;
import com.emce.authserver.exception.DuplicateEmailException;
import com.emce.authserver.repository.UserRepository;
import com.emce.commons.exception.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public AuthResponse register(RegisterRequest registerRequest) throws DataIntegrityViolationException {
        LocalDateTime now = LocalDateTime.now();
        var userCredential = UserCredential.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.USER)
                .build();
        try {
            userRepository.save(userCredential);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException(String.format("Email already exists! mail: %s", userCredential.getEmail()));
        }
        var jwtToken = jwtUtil.generateToken(userCredential);
        return AuthResponse.builder()
                .token(jwtToken)
                .expiresAt(jwtUtil.extractExpiration(jwtToken))
                .build();
    }

    public AuthResponse login(AuthRequest request) throws AuthenticationException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userService.loadUserByUsername(request.email());

        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .expiresAt(jwtUtil.extractExpiration(jwtToken))
                .build();
    }

    public void validateToken(String token) throws ExpiredJwtException {
        final String userEmail = jwtUtil.extractUsername(token);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(userEmail);
            if (jwtUtil.isTokenValid(token, userDetails)) {
                return;
            }else {
                throw new CredentialsExpiredException("Token is expired or invalid");
            }
        }
    }

    public AuthResponse update(RegisterRequest registerRequest) {
        UserCredential userCredential = userRepository.findByEmail(registerRequest.email()).orElseThrow(() -> new UserNotFoundException("user not found"));
        userCredential.setFirstName(registerRequest.firstName());
        try {
            userRepository.save(userCredential);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException(String.format("Email already exists! mail: %s", userCredential.getEmail()));
        }
        var jwtToken = jwtUtil.generateToken(userCredential);
        return AuthResponse.builder()
                .token(jwtToken)
                .expiresAt(jwtUtil.extractExpiration(jwtToken))
                .build();

    }
}
