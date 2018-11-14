package model;

import java.awt.geom.Rectangle2D;

public class Hurtbox extends Rectangle2D.Double {
    private boolean hurting;
    private int damage, knockback;
    private double relativeX, relativeY;

    public Hurtbox(double x, double y, int width, int height){
        super(x,y,width,height);
    }

    public boolean isHurting() {
        return hurting;
    }

    public int getDamage() {
        return damage;
    }

    public int getKnockback() {
        return knockback;
    }

    public void setHurting(boolean hurting) {
        this.hurting = hurting;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setKnockback(int knockback) {
        this.knockback = knockback;
    }

    public double getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(double relativeX) {
        this.relativeX = relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(double relativeY) {
        this.relativeY = relativeY;
    }

    public void setRelativeRect(double relativeX, double relativeY, double width, double height){
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.width = width;
        this.height = height;
    }
}
