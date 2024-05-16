package de.dhbw.trackingappbackend.entity.location;

import de.dhbw.trackingappbackend.control.TileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tile {

    private int xTile;

    private int yTile;

    private byte zoomLevel;

    // position of the upper-left tile corner
    private double[] position;

    public Tile(int xTile, int yTile, byte zoomLevel) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.zoomLevel = zoomLevel;
        this.position = TileService.getCoordinatesByTile(xTile, yTile, zoomLevel);
    }

    public String toString() {
        return "x" + this.xTile + ", y" + this.yTile + ", z" + this.zoomLevel + ")";
    }
}
