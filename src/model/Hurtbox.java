package model;

import java.awt.geom.Rectangle2D;

public class Hurtbox extends Rectangle2D.Double {
    private boolean hurting;
    private int damage, knockback;

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
}
