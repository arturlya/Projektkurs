package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.IRenderable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import static control.Timer.dt;

public class GravitationalObject extends Entity implements IUpdateable, IRenderable {

    protected double verticalSpeed, horizontalSpeed;
    protected boolean inAir;
    protected Rectangle2D.Double hitbox, renderHitbox;
    protected Line2D[] downLines;
    protected double gameWidth = Game.getConfiguration().graphics().getResolution().getWidth();
    protected double gameHeight = Game.getConfiguration().graphics().getResolution().getHeight();

    public GravitationalObject(){
        downLines = new Line2D[2];
        downLines[0] = new Line2D.Double();
        downLines[1] = new Line2D.Double();
        renderHitbox = new Rectangle2D.Double();
    }

    @Override
    public void update() {
        setX(getX() + horizontalSpeed * dt);
        setY(getY() + verticalSpeed * dt);
        hitbox.setRect(getX(), getY(), getWidth(), getHeight());
        downLines[0].setLine(getX(), getY() + getHeight() - 5, getX(), getY() + getHeight() + 20);
        downLines[1].setLine(getX() + getWidth(), getY() + getHeight() - 5, getX() + getWidth(), getY() + getHeight() + 20);

        if((this instanceof Player && this.hitbox.intersects(0,0,1920,1080)) || (!(this instanceof Player) && this.hitbox.intersects(-200,-200,2320,1480))) {
            renderHitbox.setRect(getX() / 1920 * gameWidth, getY() / 1080 * gameHeight, getWidth() / 1920 * gameWidth, getHeight() / 1080 * gameHeight);
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(70,120,255));
        g.fill(renderHitbox);
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

    public Rectangle2D.Double getRenderHitbox() {
        return renderHitbox;
    }

    public Line2D[] getDownLines() {
        return downLines;
    }
}
