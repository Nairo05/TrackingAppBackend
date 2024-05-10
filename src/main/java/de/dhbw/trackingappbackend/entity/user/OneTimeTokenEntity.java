package de.dhbw.trackingappbackend.entity.user;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "one_time_passwords")
public class OneTimeTokenEntity {

    @Id
    private String id;

    @Min(8)
    @Max(8)
    private int pin;

    @Email
    private String email;

    @Min(21)
    @Max(21)
    private String signature;

    @PastOrPresent
    private Instant created;

    @Min(600)
    @Max(3600)
    private int lifetime;

}
