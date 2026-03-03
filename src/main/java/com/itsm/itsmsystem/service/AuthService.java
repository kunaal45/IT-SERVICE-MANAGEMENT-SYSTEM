package com.itsm.itsmsystem.service;

import com.itsm.itsmsystem.dto.LoginRequest;
import com.itsm.itsmsystem.dto.LoginResponse;
import com.itsm.itsmsystem.exception.UnauthorizedException;
import com.itsm.itsmsystem.model.entity.User;
import com.itsm.itsmsystem.repository.UserRepository;
import com.itsm.itsmsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return LoginResponse.builder()
            .token(token)
            .email(user.getEmail())
            .name(user.getName())
            .role(user.getRole().toString())
            .userId(user.getId())
            .message("Login successful")
            .build();
    }
}
