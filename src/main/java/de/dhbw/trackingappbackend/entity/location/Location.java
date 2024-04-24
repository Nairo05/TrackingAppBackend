package de.dhbw.trackingappbackend.entity.location;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "base_location")
@Data
@AllArgsConstructor
public class Location {

    @Id
    private String id;

    private Tile tile;

    // user id of the app user
    private String appUserId; // TODO remove, obsolete (bc we want static set of locations)

    // private int countVisited;

    @Override
    public String toString() {
        return "ID: " + this.id + " Tile: " + this.tile;
    }
}
