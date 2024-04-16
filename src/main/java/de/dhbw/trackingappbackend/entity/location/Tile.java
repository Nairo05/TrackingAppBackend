package de.dhbw.trackingappbackend.entity.location;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Tile implements Serializable {

    private int xTile;

    private int yTile;

    private byte zoomLevel;

    public String toString() {
        return "x" + this.xTile + ", y" + this.yTile + ", z" + this.zoomLevel;
    }
}
