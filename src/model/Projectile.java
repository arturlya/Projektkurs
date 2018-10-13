package model;

import java.awt.geom.Rectangle2D;

/**
 * Created by Marcel on 20.07.2018.
 */
public class Projectile extends GravitationalObject {

    private Hurtbox hurtbox;
    private Player player;

    public Projectile(Player player, double x, double y, int width, int height){
        super();
        this.player = player;
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        hitbox = new Rectangle2D.Double(x,y,width,height);
        hurtbox = new Hurtbox(x,y,width,height);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        if(player.getLookingAt() == 1 ){
            setHorizontalSpeed(500);
        }else{
            setHorizontalSpeed(-500);
        }
    }

    public Hurtbox getHurtbox() {
        return hurtbox;
    }
}
