package com.squad1.hackathon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    @NotBlank(message = "Password is required")
    @Size(min = 2, message = "Password must be at least 6 characters")
    private String password;
}