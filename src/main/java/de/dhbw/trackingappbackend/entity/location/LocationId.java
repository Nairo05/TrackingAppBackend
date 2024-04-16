package de.dhbw.trackingappbackend.entity.location;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "base_location_id")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LocationId implements Serializable {

    private int xTile;

    private int yTile;

    public String toString() {
        return "X: " + this.xTile + " Y: " + this.yTile;
    }
}
