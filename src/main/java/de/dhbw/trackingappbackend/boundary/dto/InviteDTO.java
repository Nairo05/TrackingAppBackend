package de.dhbw.trackingappbackend.boundary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InviteDTO {

    private String uuid;
    private String email;
    private String sendAt;

}
