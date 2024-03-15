package de.dhbw.trackingappbackend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtResponse {

    @NotBlank
    @NonNull
    private String token;

    @NotBlank
    @NonNull
    private String type="Bearer";

    @NonNull
    private String email;

    private Long id;

}
