package de.dhbw.trackingappbackend.boundary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendDTO {

    private String uuid;

    private String email;

    //TODO
    private String username;

    private String acceptedAt;

    //TODO in %
    private float stats;

}
