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

    // tile identifier by x, y and zoom level
    private Tile tile;

    // user id of the app user
    private String appUserId;

    // position of the upper-left tile corner
    private double[] position; // TODO change to lower left corner?

    @Override
    public String toString() {
        return "ID: " + this.id + " Position: " + this.position[0] + "," + this.position[1];
    }
}
