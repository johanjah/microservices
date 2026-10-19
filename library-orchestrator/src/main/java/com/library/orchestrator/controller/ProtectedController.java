package com.library.orchestrator.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProtectedController {

    @GetMapping("/api/me")
    public Map<String, String> getCurrentUser(
            Authentication authentication
    ) {
        return Map.of(
                "username", authentication.getName(),
                "message", "Kamu berhasil terautentikasi"
        );
    }
}