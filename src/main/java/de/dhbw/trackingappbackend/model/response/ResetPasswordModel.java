package de.dhbw.trackingappbackend.model.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public class ResetPasswordModel {

    @NotBlank
    @UUID
    private String resetToken;

    @NotBlank
    @Size(min=8, max = 120)
    private String password;

    @Email
    private String email;

}
