package de.dhbw.trackingappbackend.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatDTO {

    private String kuerzel;
    private float percentage;
}
