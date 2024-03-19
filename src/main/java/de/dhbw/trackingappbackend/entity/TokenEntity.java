package de.dhbw.trackingappbackend.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "token")
public class TokenEntity {

    @Id
    private String id;

    @NotNull
    private String userId;

    @NotNull
    private String refreshToken;

    @NotNull
    private Instant expiryDate;

}
