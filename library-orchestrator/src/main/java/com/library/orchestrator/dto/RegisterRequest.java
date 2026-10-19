package com.library.orchestrator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Username wajib diisi")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "Email wajib diisi")
        @Email(message = "Format email tidak valid")
        @Size(max = 100)
        String email,

        @NotBlank(message = "Password wajib diisi")
        @Size(min = 8, max = 72)
        String password

) {
}