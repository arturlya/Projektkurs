package model;


import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.IRenderable;

import java.awt.*;

public class Test extends Entity implements IRenderable {

    public Test(){
        setX(50);
        setY(50);
        setWidth(100);
        setHeight(100);
    }


    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.drawRect(50,50,50,50);
    }
}
