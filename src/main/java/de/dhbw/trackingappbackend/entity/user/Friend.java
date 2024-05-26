package de.dhbw.trackingappbackend.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    public static int open = 0;
    public static int accepted = 1;

    private String uuid;

    private int status;

    private String email;

    private Instant sendAt;

    private Instant acceptedAt;

    private float germanyPercentage;

    private String username;

}
