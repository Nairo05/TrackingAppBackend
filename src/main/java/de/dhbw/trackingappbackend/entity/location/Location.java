package de.dhbw.trackingappbackend.entity.location;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "base_location")
@Data
@AllArgsConstructor
@IdClass(LocationId.class)
public class Location {

    @EmbeddedId
    private LocationId locationId;

    private String appUserId;

    private double[] position;

    @Override
    public String toString() {
        return "ID: " + this.locationId + " Position: " + this.getPosition()[0] + "," + this.getPosition()[1];
    }
}
