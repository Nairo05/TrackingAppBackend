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

        this.xTile = location.getTileId().getXTile();
        this.yTile = location.getTileId().getYTile();
        this.zoomLevel = location.getTileId().getZoomLevel();

        this.posUpperLeft = location.getPosition();
        this.posUpperRight = TileService.getCoordinatesByTileId(new TileId(this.xTile + 1, this.yTile, this.zoomLevel));
        this.posLowerRight = TileService.getCoordinatesByTileId(new TileId(this.xTile + 1, this.yTile + 1, this.zoomLevel));
        this.posLowerLeft = TileService.getCoordinatesByTileId(new TileId(this.xTile, this.yTile + 1, this.zoomLevel));
    }
}
