package de.dhbw.trackingappbackend.entity.location;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "base_location")
@Data
@AllArgsConstructor
@IdClass(TileId.class)
public class Location {

    // unique tile identifier by x, y and zoom level
    @EmbeddedId
    private TileId tileId;

    // user id of the app user
    private String appUserId;

    // position of the upper-left tile corner
    private double[] position; // TODO change to lower left corner?

    @Override
    public String toString() {
        return "ID: " + this.tileId + " Position: " + this.position[0] + "," + this.position[1];
    }
}
