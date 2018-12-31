package model;

import de.gurkenlabs.litiengine.util.geom.Vector2D;

import java.awt.geom.Rectangle2D;

/**
 * Created by Marcel on 20.07.2018.
 */
public class Projectile extends GravitationalObject {

    private Hurtbox hurtbox;
    private Player player;

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

    @Override
    public void update() {
        super.update();
        hurtbox.setRect(getX(),getY(),getWidth(),getHeight());
    }

    public Hurtbox getHurtbox() {
        return hurtbox;
    }

    public Player getPlayer(){return player;}
}
