package de.dhbw.trackingappbackend.model.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class ResetPasswordModel {

    @NotBlank
    private String pin;

    @NotBlank
    @Size(min=8, max = 120)
    private String password;

    @Email
    private String email;

}
