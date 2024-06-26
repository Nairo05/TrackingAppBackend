package de.dhbw.trackingappbackend.entity.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.dhbw.trackingappbackend.control.TileService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LocationWrapper {

    @JsonIgnore
    private int xTile;
    @JsonIgnore
    private int yTile;
    @JsonIgnore
    private boolean mergedVertically;
    @JsonIgnore
    private boolean mergedHorizontally;

    private byte zoomLevel;

    private double[] posUpperLeft;
    private double[] posUpperRight;
    private double[] posLowerRight;
    private double[] posLowerLeft;

    public LocationWrapper(Tile tile) {

        this.xTile = tile.getXTile();
        this.yTile = tile.getYTile();
        this.zoomLevel = tile.getZoomLevel();
        this.mergedVertically = false;
        this.mergedHorizontally = false;

        this.posUpperLeft = tile.getPosition();
        this.posUpperRight = TileService.getCoordinatesByTile(xTile + 1, yTile, zoomLevel);
        this.posLowerRight = TileService.getCoordinatesByTile(xTile + 1, yTile + 1, zoomLevel);
        this.posLowerLeft = TileService.getCoordinatesByTile(xTile, yTile + 1, zoomLevel);
    }

    public LocationWrapper(Location location) {
        this(location.getTile());
    }
}
