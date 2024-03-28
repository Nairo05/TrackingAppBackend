package de.dhbw.trackingappbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "base_location")
@Data
@AllArgsConstructor
public class Location {

    @Id
    private String id;

    @NonNull
    private String appUserId; // TODO feld eigentlich nicht sinnvoll

    @NonNull
    private double[] position;

    @Override
    public String toString() {
        return "ID: " + this.getId() + " Position: " + this.getPosition()[0] + "," + this.getPosition()[1];
    }
}
