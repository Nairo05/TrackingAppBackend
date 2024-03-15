package de.dhbw.trackingappbackend.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min=8, max = 120)
    private String password;

    @NotBlank
    @Size(min=3, max = 20)
    private String firstname;

    @NotBlank
    @Size(min=3, max = 20)
    private String lastname;

}
