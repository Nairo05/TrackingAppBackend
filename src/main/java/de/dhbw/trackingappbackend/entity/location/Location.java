package de.dhbw.trackingappbackend.entity.location;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "base_location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    private String id;

    private Tile tile;

    private List<String> kuerzel;

    public Location(Tile tile) {
        this.tile = tile;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " Tile: " + this.tile + " Land: " + this.kuerzel;
    }
}
