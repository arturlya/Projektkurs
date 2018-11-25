package model.Maps;

import de.gurkenlabs.litiengine.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Map1 extends Map {

    public Map1() {
        super();
        try {
            map = ImageIO.read(new File("assets/maps/map1.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        lines = new ArrayList<>();
        spawnpoints = new ArrayList<>();
        createHitBoxes();
        createSpawnpoints();
    }

    @Override
    public void render(Graphics2D graphics2D) {
        if (Game.getConfiguration().graphics().isFullscreen()) {
            width = (int) Game.getConfiguration().graphics().getResolution().getWidth();
            height = (int) Game.getConfiguration().graphics().getResolution().getHeight();
        } else width = Game.getConfiguration().graphics().getResolutionWidth();
        height = Game.getConfiguration().graphics().getResolutionHeight();

        graphics2D.drawImage(map, 0, 0, width, height, null);
    }

    private void createHitBoxes() {
        lines.add(new Line2D.Double(384, 640, 1407, 640));
        lines.add(new Line2D.Double(0, 384, 255, 384));
        lines.add(new Line2D.Double(1600, 320, 1920, 320));
    }

    private void createSpawnpoints() {
        spawnpoints.add(new Point(530, 540));
        spawnpoints.add(new Point(1250, 540));
        spawnpoints.add(new Point(110, 260));
        spawnpoints.add(new Point(1770, 200));
    }
}
