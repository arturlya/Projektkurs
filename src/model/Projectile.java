package model;

import de.gurkenlabs.litiengine.util.geom.Vector2D;

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
    public Projectile(Player player, double x, double y, int width, int height, Vector2D direction, int damage, int knockback){
        super();
        this.player = player;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
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
     * @return Gibt die Hurtbox des Projektils zurück.
     */
    public Hurtbox getHurtbox() {
        return hurtbox;
    }

    /**
     * @return Gibt den Besitzer des Projektils zurück
     */
    public Player getPlayer(){return player;}
}
