package model;

import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.IRenderable;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static control.Timer.dt;

public class GravitationalObject extends Entity implements IUpdateable, IRenderable {

    protected double verticalSpeed, horizontalSpeed;
    protected boolean inAir;
    protected Rectangle2D.Double hitbox;

    @Override
    public void update() {
        setX(getX() + horizontalSpeed * dt);
        setY(getY() + verticalSpeed * dt);
        hitbox.setRect(this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(70,120,255));
        g.fill(hitbox);
    }


    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public void setHorizontalSpeed(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public boolean isInAir() {
        return inAir;
    }

    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle2D.Double hitbox) {
        this.hitbox = hitbox;
    }

}
