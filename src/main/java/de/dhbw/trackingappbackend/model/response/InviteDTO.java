package de.dhbw.trackingappbackend.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InviteDTO {

    private String uuid;
    private String email;
    private String sendAt;

}
