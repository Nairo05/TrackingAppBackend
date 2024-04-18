package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.control.TileService;
import de.dhbw.trackingappbackend.entity.location.Tile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class TileServiceTest {

    @Test
    public void testGetTileByCoordinates() {

        byte zoomLevel = 14;
        Tile horbTile = new Tile(8587, 5664, (byte) 14);

        double[][] posInsideTile = {{48.4454875733775, 8.69686010925506},
            {48.457797024488215, 8.67954349514507}, {48.45811427856263, 8.700582872351122},
            {48.44386337246175, 8.69735805996525}, {48.445557642195034, 8.679635453838522}};

        double[][] posOutsideTile = {{48.46061932497518, 8.690445017836884}, {48.451083794580086, 8.702339847116127},
            {48.44274746413431, 8.69067663628975}, {48.45218888715979, 8.675443054698661}};

        for (double[] pos : posInsideTile) {
            assert(TileService.getTileByCoordinates(pos[0], pos[1], zoomLevel).equals(horbTile));
        }

        for (double[] pos : posOutsideTile) {
            assert(!TileService.getTileByCoordinates(pos[0], pos[1], zoomLevel).equals(horbTile));
        }
    }

    @Test
    public void testGetCoordinatesByTile() {

        int xHorb = 8587;
        int yHorb = 5664;
        byte zoomLevel = 14;

        double[] posUpperLeft = {48.45835188280866, 8.67919921875};
        double[] posUpperRight = {48.45835188280866, 8.701171875};
        double[] posLowerRight = {48.44377831058803, 8.701171875};
        double[] posLowerLeft = {48.44377831058803, 8.67919921875};

        double[] posUl = TileService.getCoordinatesByTile(xHorb, yHorb, zoomLevel);
        double[] posUr = TileService.getCoordinatesByTile(xHorb + 1, yHorb, zoomLevel);
        double[] posLr = TileService.getCoordinatesByTile(xHorb + 1, yHorb + 1, zoomLevel);
        double[] posLl = TileService.getCoordinatesByTile(xHorb, yHorb + 1, zoomLevel);

        assert(Arrays.equals(posUpperLeft, posUl));
        assert(Arrays.equals(posUpperRight, posUr));
        assert(Arrays.equals(posLowerRight, posLr));
        assert(Arrays.equals(posLowerLeft, posLl));
    }
}
