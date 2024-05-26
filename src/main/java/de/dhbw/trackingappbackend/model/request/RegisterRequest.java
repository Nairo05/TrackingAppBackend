package de.dhbw.trackingappbackend.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Valid
public class RegisterRequest {

    @Email
    @Size(max = 50)
    private String email;

    @Size(min=6, max = 50)
    private String username;

    @Size(min=6, max = 120)
    private String password;

    @Size(min=3, max = 20)
    private String firstname;

    @Size(min=3, max = 20)
    private String lastname;

    private String cipher;

}
