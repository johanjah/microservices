package com.library.orchestrator.controller;

import com.library.orchestrator.dto.LoginRequest;
import com.library.orchestrator.dto.LoginResponse;
import com.library.orchestrator.dto.RegisterRequest;
import com.library.orchestrator.dto.RegisterResponse;
import com.library.orchestrator.security.JwtService;
import com.library.orchestrator.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        Long userId = authService.register(request);

        RegisterResponse response = new RegisterResponse(
                userId,
                "Registrasi berhasil"
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String token = authService.login(
                request.username(),
                request.password()
        );

        LoginResponse response = new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds()
        );

        return ResponseEntity.ok(response);
    }

}
