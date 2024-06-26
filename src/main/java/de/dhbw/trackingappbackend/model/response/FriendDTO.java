package de.dhbw.trackingappbackend.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendDTO {

    private String uuid;

    private String email;

    private String username;

    private String acceptedAt;

    private float germanyPercentage;

}
