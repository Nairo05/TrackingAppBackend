package de.dhbw.trackingappbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShownUserModel {

    private String uuid;

    private String shownName;

}
