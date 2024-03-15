package de.dhbw.trackingappbackend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordModel {

    @NotBlank
    private String email;

}
