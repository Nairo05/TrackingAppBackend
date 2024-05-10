package de.dhbw.trackingappbackend.boundary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendDTO {

    private String uuid;

    private String email;

    private String acceptedAt;

    private float stats;

}
