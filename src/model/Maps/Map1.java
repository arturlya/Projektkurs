package model.Maps;

import de.gurkenlabs.litiengine.Game;
import model.StaticData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse Map1
 * Vererbt von der Klasse Map
 *
 */
public class Map1 extends Map {

    /**
     * Konstruktor der Klasse Map1
     */
    public Map1() {
        super();
        try {
            map = ImageIO.read(new File("assets/maps/map1.png"));
            bg = ImageIO.read(new File("assets/maps/bg1.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        lines = new ArrayList<>();
        spawnpoints = new ArrayList<>();
        createHitBoxes();
        createSpawnpoints();
        width = StaticData.ScreenWidth;
        height = StaticData.ScreenHeight;
        widthMultiplier = StaticData.ScreenWidthMultiplier;
        heightMultiplier = StaticData.ScreenHeightMultiplier;
    }

    /**
     * Spezifische Hitboxen initialisieren
     */
    private void createHitBoxes() {
        lines.add(new Line2D.Double(384, 640, 1407, 640));
        lines.add(new Line2D.Double(0, 384, 255, 384));
        lines.add(new Line2D.Double(1600, 320, 1920, 320));
    }

    /**
     * Spezifische Spawnpoints initialisieren
     */
    private void createSpawnpoints() {
        spawnpoints.add(new Point(530, 540));
        spawnpoints.add(new Point(1250, 540));
        spawnpoints.add(new Point(110, 260));
        spawnpoints.add(new Point(1770, 200));
    }
}
