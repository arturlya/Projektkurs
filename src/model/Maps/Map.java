package model.Maps;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.IRenderable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Map extends Entity implements IRenderable {

    Image map,bg;
    protected int width,height;
    protected ArrayList<Point> spawnpoints;
    ArrayList<Line2D.Double> lines;

    public Map(){

    }

    @Override
    public void render(Graphics2D graphics2D) {

    }

    public ArrayList<Point> getSpawnpoints() {
        return spawnpoints;
    }

    public ArrayList<Line2D.Double> getLines() {
        return lines;
    }
}
