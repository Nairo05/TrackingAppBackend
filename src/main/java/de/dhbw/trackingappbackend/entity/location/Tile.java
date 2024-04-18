package de.dhbw.trackingappbackend.entity.location;

import de.dhbw.trackingappbackend.control.TileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tile {

    // x tile id
    private int xTile;

    // y tile id
    private int yTile;

    // zoom level of the tile
    private byte zoomLevel;

    // position of the upper-left tile corner
    private double[] position; // TODO change to lower left corner?

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
