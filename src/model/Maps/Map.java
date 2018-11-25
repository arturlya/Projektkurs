package model.Maps;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.IRenderable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Map extends Entity implements IRenderable {

    Image map;
    protected int width,height;
    protected ArrayList<Point> spawnpoints;
    ArrayList<Line2D.Double> lines;

    public Map(){
        if (Game.getConfiguration().graphics().isFullscreen()){
            width = (int)Game.getConfiguration().graphics().getResolution().getWidth();
            height = (int)Game.getConfiguration().graphics().getResolution().getHeight();
        }else width = Game.getConfiguration().graphics().getResolutionWidth(); height = Game.getConfiguration().graphics().getResolutionHeight();
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
