package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.location.Tile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Tag(name = "Tile Service")
@Service
@RequiredArgsConstructor
public class TileService {

    /**
     * Returns the tile based on the given coordinates and zoom level.
     *
     * @link <a href="https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames">Calculation Method</a>
     *
     * @param latitude given latitude
     * @param longitude given longitude
     * @param zoomLevel given zoom level
     * @return tile
     */
    public static Tile getTileByCoordinates(double latitude, double longitude, byte zoomLevel) {

        int n = (int) Math.pow(2, zoomLevel);
        int xTile = (int) Math.floor((longitude + 180) / 360 * n);
        int yTile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(latitude)) + 1 / Math.cos(Math.toRadians(latitude))) / Math.PI) / 2 * n);

        return new Tile(xTile, yTile, zoomLevel);
    }

    /**
     * Returns the upper-left coordinates of a tile based on the given tile.
     * @param tile given tile
     * @return upper-left coordinate pair
     */
    public static double[] getCoordinatesByTile(Tile tile) {

        int n = (int) Math.pow(2, tile.getZoomLevel());
        double lon = tile.getXTile() * 360.0 / n - 180;
        double lat = Math.toDegrees(Math.atan(Math.sinh(Math.PI * (1 - 2 * tile.getYTile() / (double) n))));

        return new double[]{lat, lon};
    }
}
