package de.dhbw.trackingappbackend.entity.location;

import de.dhbw.trackingappbackend.control.TileService;
import lombok.Data;

@Data
public class LocationWrapper {

    private int xTile;

    private int yTile;

    private byte zoomLevel;

    private double[] posUpperLeft;
    private double[] posUpperRight;
    private double[] posLowerRight;
    private double[] posLowerLeft;


    public LocationWrapper(Location location) {

        this.xTile = location.getTile().getXTile();
        this.yTile = location.getTile().getYTile();
        this.zoomLevel = location.getTile().getZoomLevel();

        this.posUpperLeft = location.getPosition();
        this.posUpperRight = TileService.getCoordinatesByTile(new Tile(this.xTile + 1, this.yTile, this.zoomLevel));
        this.posLowerRight = TileService.getCoordinatesByTile(new Tile(this.xTile + 1, this.yTile + 1, this.zoomLevel));
        this.posLowerLeft = TileService.getCoordinatesByTile(new Tile(this.xTile, this.yTile + 1, this.zoomLevel));
    }
}
