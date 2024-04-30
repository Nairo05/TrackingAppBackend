package de.dhbw.trackingappbackend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Size(min = 4, max = 120)
    private String username;

    @Email
    @Size(min = 4, max = 120)
    private String email;

    @Size(min=8, max = 50)
    @NotBlank
    private String password;
}
