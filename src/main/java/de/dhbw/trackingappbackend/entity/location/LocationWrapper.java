package de.dhbw.trackingappbackend.entity.location;

import de.dhbw.trackingappbackend.control.TileService;
import lombok.Data;

@Data
public class LocationWrapper {

    private int xTile;
    private int yTile;
    private byte zoomLevel;
    private byte opacity;

    private double[] posUpperLeft;
    private double[] posUpperRight;
    private double[] posLowerRight;
    private double[] posLowerLeft;

    public LocationWrapper(Location location) {

        this.xTile = location.getTile().getXTile();
        this.yTile = location.getTile().getYTile();
        this.zoomLevel = location.getTile().getZoomLevel();
        this.opacity = 0; // TODO not necessarily 0, different zoomLevels will change the oppacity

        this.posUpperLeft = location.getTile().getPosition();
        this.posUpperRight = TileService.getCoordinatesByTile(xTile + 1, yTile, zoomLevel);
        this.posLowerRight = TileService.getCoordinatesByTile(xTile + 1, yTile + 1, zoomLevel);
        this.posLowerLeft = TileService.getCoordinatesByTile(xTile, yTile + 1, zoomLevel);
    }
}
