package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerPatchDTO {

    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 50, message = "Phone number must be less than 50 characters")
    private String phone;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    private String status;
}
