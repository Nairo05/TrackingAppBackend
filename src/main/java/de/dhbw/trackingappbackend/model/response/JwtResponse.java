package de.dhbw.trackingappbackend.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtResponse {

    @NonNull
    private String token;

    @NonNull
    @JsonProperty("refresh_token")
    private String refreshToken;

    @NonNull
    private String type="Bearer";

    @NonNull
    private String email;

    @JsonIgnore
    private Long id;
}
