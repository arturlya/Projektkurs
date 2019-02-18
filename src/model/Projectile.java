package model;

import de.gurkenlabs.litiengine.util.geom.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Klasse Projectile erstellt ein Projektil für einen Spieler.
 *
 * Created by Marcel on 20.07.2018.
 */
public class Projectile extends GravitationalObject {

    /** Hurtbox des Projektils*/
    private Hurtbox hurtbox;
    /** Erzeuger des Projektils*/
    private Player player;
    /** Bild des Projektils*/
    private Image image;

    /**
     * Konstruktor der Klasse Projectile.
     *
     * @param player Erzeuger des Projektils
     * @param x X-Position des Projektils
     * @param y Y-Position des Projektils
     * @param width Breite des Projektils
     * @param height Höhe des Projektils
     * @param direction Richtung, in die das Projektil fliegt
     * @param damage Schaden des Projektils
     * @param knockback Rückstoß, den das Projektil ausübt
     */
    public Projectile(Player player, double x, double y, int width, int height, Image image, Vector2D direction, int damage, int knockback){
        super();
        this.player = player;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        if (image != null) this.image = image;
        hitbox = new Rectangle2D.Double(x,y,width,height);
        hurtbox = new Hurtbox(x,y,width,height);
        hurtbox.setDamage(damage);
        hurtbox.setKnockback(knockback);
        if(player.getLookingAt() == 0) {
            setHorizontalSpeed(-direction.getX());
        }else{
            setHorizontalSpeed(direction.getX());
        }
        setVerticalSpeed(direction.getY());
        inAir = true;
    }

    /**
     * Update des Interfaces IUpdateable
     */
    @Override
    public void update() {
        super.update();
        hurtbox.setRect(getX(),getY(),getWidth(),getHeight());
    }

    /**
     * Render des Interfaces IRenderable
     * Rendert das Projektil als Bild wenn ein vorhandes ist
     * Wenn nicht dann nur das Rechteck (die Hurtbox)
     * @param g
     */
    @Override
    public void render(Graphics2D g) {
        if (image != null) g.drawImage(image,(int)(getX()*StaticData.ScreenWidthMultiplier),(int)(getY()*StaticData.ScreenHeightMultiplier),null);
        else g.fillRect((int)(getX()*StaticData.ScreenWidthMultiplier),(int)(getY()*StaticData.ScreenHeightMultiplier),(int)(getWidth()*StaticData.ScreenWidthMultiplier),(int)(getHeight()*StaticData.ScreenHeightMultiplier));
    }

    /**
     * @return Gibt die Hurtbox des Projektils zurück.
     */
    public Hurtbox getHurtbox() {
        return hurtbox;
    }

    /**
     * @return Gibt den Besitzer des Projektils zurück
     */
    public Player getPlayer(){return player;}

    /**
     * Setzt das momentane Image auf ein anderes
     */
    public void setImage(Image image) {
        this.image = image;
    }
}
