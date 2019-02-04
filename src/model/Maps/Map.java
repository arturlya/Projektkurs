package model.Maps;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.IRenderable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Klasse Map
 * Zuständig für die Vererbung notwendiger Attribute etc. an spezifische Maps.
 */
public class Map extends Entity implements IRenderable {

    /** Eine Map braucht eine Map und einen Hintergrund*/
    Image map,bg;
    /** Screen-Breite und Höhe*/
    protected int width,height;
    /** Anppasung der Auflösuung falls nicht 1920x1080*/
    protected float widthMultiplier,heightMultiplier;
    /** Jede Map braucht Spawnpoints*/
    protected ArrayList<Point> spawnpoints;
    /** Hitboxen der Map*/
    ArrayList<Line2D.Double> lines;

    /**
     * Konstruktor der Klasse Map
     */
    public Map(){

    }

    /**
     * Render Methode von IRenderable
     * Rendert die Map und den Hintergrund
     * @param graphics2D
     */
    @Override
    public void render(Graphics2D graphics2D) {
        graphics2D.drawImage(bg, 0, 0, width, height, null);
        graphics2D.drawImage(map, 0, 0, width, height, null);
    }

    /**
     * Rückgebende Methode für die Spawnpoints
     * @return spawnpoints (ArrayList mit Point-Objekten)
     */
    public ArrayList<Point> getSpawnpoints() {
        return spawnpoints;
    }

    /**
     * Rückgebende Methode für die Hitboxen
     * @return lines (ArrayList mit Line2D-Objekten)
     */
    public ArrayList<Line2D.Double> getLines() {
        return lines;
    }
}
