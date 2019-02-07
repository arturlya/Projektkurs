package model;

import java.awt.geom.Rectangle2D;

/**
 * Klasse Hurtbox
 * Erbt von Rectangle2D.Double
 * Gibt an, wo und wie der Angriff eines Spielers trifft
 */
public class Hurtbox extends Rectangle2D.Double {

    /**Merkt sich, ob die Hurtbox 'aktiv' ist*/
    private boolean hurting;
    /**Merkt sich den Schaden und den Rückstoß des Angriffs*/
    private int damage, knockback;
    /**Merkt sich die x und y Koordinate Relativ zum Spieler gesehen*/
    private double relativeX, relativeY;

    /**
     * Konstuktor der Hurtbox
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param width Breite
     * @param height Höhe
     */
    public Hurtbox(double x, double y, int width, int height){
        super(x,y,width,height);
    }

    /**@return gibt an ob die Hurtbox 'aktiv' ist*/
    public boolean isHurting() {
        return hurting;
    }

    /**@return gibt den Schaden zurück*/
    public int getDamage() {
        return damage;
    }

    /**@return gibt den Rückstoß zurück*/
    public int getKnockback() {
        return knockback;
    }

    /**@param hurting setzt hurting auf diesen Wert*/
    public void setHurting(boolean hurting) {
        this.hurting = hurting;
    }

    /**@param damage setzt damage auf diesen Wert*/
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**@param knockback setzt knockback auf diesen Wert*/
    public void setKnockback(int knockback) {
        this.knockback = knockback;
    }

    /**@return gibt die relative X-Koordinate zurück*/
    public double getRelativeX() {
        return relativeX;
    }

    /**@return gibt die relative Y-Koordinate zurück*/
    public double getRelativeY() {
        return relativeY;
    }

    /** setzt die Hurtbox auf die angegeben Koordinaten und Maße*/
    public void setRelativeRect(double relativeX, double relativeY, double width, double height){
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.width = width;
        this.height = height;
    }
}
