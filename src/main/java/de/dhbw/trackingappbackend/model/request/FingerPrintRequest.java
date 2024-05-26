package de.dhbw.trackingappbackend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.web.PortResolverImpl;

@Data
public class FingerPrintRequest {

    @Email
    @Size(min = 4, max = 120)
    private String email;

    @NotNull
    private String cipher;

}
