package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record BorrowerPatchDTO(
        @Size(max = 255, message = "Name must be less than 255 characters")
        String name,

        @Email(message = "Invalid email format")
        String email,

        @Size(max = 50, message = "Phone number must be less than 50 characters")
        String phone,

        @Size(max = 255, message = "Address must be less than 255 characters")
        String address,

        String status
) {}
