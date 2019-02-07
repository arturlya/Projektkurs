package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Entity;
import de.gurkenlabs.litiengine.graphics.IRenderable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import static control.Timer.dt;

/**
 * Klasse GravitationalObject
 * Erbt von Entity
 * Implementiert IUpdatable und IRenderable
 */
public class GravitationalObject extends Entity implements IUpdateable, IRenderable {

    /** Merkt sich die vertikale und horizontale Geschwindigkeit*/
    protected double verticalSpeed, horizontalSpeed;
    /** Merkt sich, ob das Objekt in der Luft ist*/
    protected boolean inAir;
    /** Merkt sich die Hitbox des Objekts und die renderHitbox, die Abhängig von Fenstergröße gescaled wird*/
    protected Rectangle2D.Double hitbox, renderHitbox;
    /** Merkt sich zwei Linien unterhalb des Objekts, die dazu genutzt werden um den Fall zu stoppen*/
    protected Line2D[] downLines;
    /** Merkt sich die Breite des Fensters*/
    protected double gameWidth = Game.getConfiguration().graphics().getResolution().getWidth();
    /** Merkt sich die Höhe des Fensters*/
    protected double gameHeight = Game.getConfiguration().graphics().getResolution().getHeight();

    /**
     * Konstruktor des GravitationalObjects
     */
    public GravitationalObject(){
        downLines = new Line2D[2];
        downLines[0] = new Line2D.Double();
        downLines[1] = new Line2D.Double();
        renderHitbox = new Rectangle2D.Double();
    }


    /**
     * Update des Interfaces IUpdateable
     * Bewegt das Objekt abhängig von Geschwindigkeiten
     */
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

    /**
     * Render des Interfaces IRenderable
     * Zeichnet das Object, bzw die renderHitbox
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(70,120,255));
        if(!(this instanceof Player)) g.fill(renderHitbox);
    }

    /**@return gibt die vertikale Geschwindigkeit zurück */
    public double getVerticalSpeed() {
        return verticalSpeed;
    }

    /**@param verticalSpeed setzt die vertikale Geschwindigkeit auf diesen Wert*/
    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }

    /**@return gibt die horizontale Geschwindigkeit zurück */
    public double getHorizontalSpeed() {
        return horizontalSpeed;
    }

    /**@param horizontalSpeed setzt die horizontale Geschwindigkeit auf diesen Wert*/
    public void setHorizontalSpeed(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    /**@return gibt an, ob das Objekt in der Luft ist*/
    public boolean isInAir() {
        return inAir;
    }

    /**@param inAir setzt inAir auf diesen Wert*/
    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }

    /**@return gibt die hitbox zurück*/
    public Rectangle2D.Double getHitbox() {
        return hitbox;
    }

    /**@return gibt die renderHitbox zurück*/
    public Rectangle2D.Double getRenderHitbox() {
        return renderHitbox;
    }

    /**@return gibt die Linien unter dem Objekt zurück*/
    public Line2D[] getDownLines() {
        return downLines;
    }
}
