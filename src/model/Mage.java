package model;

import de.gurkenlabs.litiengine.util.geom.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasse Warrior.
 * Erbt von der abtrakten Oberklasse Player
 */
public class Mage extends Player{

    /**Merkt sich, ob der Teleport vom Mage benutzt wurde*/
    private boolean teleUsed;

    private BufferedImage fireball;

    /**
     * Konstruktor der Klasse Mages.
     *
     * @param x X-Position des Mages
     * @param y Y-Position des Mages
     * @param isPlayerTester gibt an, ob der Spieler im PlayerTester oder im GameClient erstellt wurde
     */
    public Mage(double x, double y, boolean isPlayerTester){
        super(x,y,isPlayerTester);
        setWidth(96);
        setHeight(96);
        setName("Mage");
        try {
            fireball = ImageIO.read(new File("assets/img/ingame/Players/Mage/Projektile/fireball.png"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update des Interfaces IUpdateable
     */
    @Override
    public void update() {
        super.update();
        if(!inAir){
            teleUsed = false;
        }
    }

    /**
     * Implementation der normalAttackRun vom Player.
     */
    @Override
    public void normalAttackRun() {
        if(lookingAt == 1){
            hurtbox.setRelativeRect(hitbox.width,hitbox.height*0.25,hitbox.width,hitbox.height*0.5);
            pr.triggerAnimation("NormalAttackRun",0.4,0,0);
        }else{
            hurtbox.setRelativeRect(-hitbox.width,hitbox.height*0.25,hitbox.width,hitbox.height*0.5);
            pr.triggerAnimation("NormalAttackRun",0.4,-hitbox.width,0);
        }
        hurtbox.setDamage(3);
        hurtbox.setKnockback(3);
        attackWindUp = 0.1;
        attackHurtTime = 0.3;
        attackWindDown  =0.6;
    }

    /**
     * Implementation der normalAttackDown vom Player.
     */
    @Override
    public void normalAttackDown() {
        hurtbox.setRelativeRect(-hitbox.width,hitbox.height*0.7,hitbox.width*3,hitbox.height*0.3);
        pr.triggerAnimation("NormalAttackDown",0.7,-hitbox.width,0);
        hurtbox.setDamage(5);
        hurtbox.setKnockback(3);
        attackWindUp = 0.1;
        attackHurtTime = 0.6;
        attackWindDown  =0.7;
    }

    /**
     * Implementation der normalAttackUp vom Player.
     */
    @Override
    public void normalAttackUp() {
        hurtbox.setRelativeRect(-hitbox.width*0.2,-hitbox.height,hitbox.width*1.4,hitbox.height);
        pr.triggerAnimation("NormalAttackUp",0.9,-hitbox.width*0.2,-hitbox.height);
        hurtbox.setDamage(4);
        hurtbox.setKnockback(2);
        attackWindUp = 0.5;
        attackHurtTime = 0.4;
        attackWindDown  =0.2;
    }

    /**
     * Implementation der normalAttackStand vom Player.
     */
    @Override
    public void normalAttackStand() {
        if(lookingAt == 1) {
            hurtbox.setRelativeRect(hitbox.width,hitbox.height*0.25,hitbox.width*1.4,hitbox.height*0.5);
            pr.triggerAnimation("NormalAttackStand",0.7,0,0);
        }else{
            hurtbox.setRelativeRect(-hitbox.width*1.4,hitbox.height*0.25,hitbox.width*1.4,hitbox.height*0.5);
            pr.triggerAnimation("NormalAttackStand",0.7,-hitbox.width*1.4,0);
        }
        hurtbox.setDamage(5);
        hurtbox.setKnockback(3);
        attackWindUp = 0.5;
        attackHurtTime = 0.2;
        attackWindDown  =0.3;
    }

    /**
     * Implementation der specialAttackRun vom Player.
     */
    @Override
    public void specialAttackRun() {
        pr.triggerAnimation("SpecialAttackRun",0.2,0,0);
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,fireball.getWidth(),fireball.getHeight(),fireball,new Vector2D(1000,-200),5,2);
        attackWindDown = 0.3;
    }

    /**
     * Implementation der specialAttackDown vom Player.
     */
    @Override
    public void specialAttackDown() {
        pr.triggerAnimation("SpecialAttackDown",0.5,0,0);
        attackWindUp = 0.5;
        shieldActive = true;
        attackWindDown = 1;
    }

    /**
     * Implementation der specialAttackUp vom Player.
     */
    @Override
    public void specialAttackUp() {
        if(!teleUsed) {
            this.setY(this.getY() - hitbox.height * 2);
            inAir = true;
            this.verticalSpeed = -500;
            teleUsed = true;
        }
    }

    /**
     * Implementation der specialAttackStand vom Player.
     */
    @Override
    public void specialAttackStand() {
        hurtbox.setRelativeRect(-hitbox.width*1.5,-hitbox.height*0.5,hitbox.width*4,hitbox.height*1.5);
        pr.triggerAnimation("SpecialAttackStand",0.8,-hitbox.width*1.5,-hitbox.height*0.5);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(7);
        attackWindUp = 0.5;
        attackHurtTime = 0.3;
        attackWindDown  =0.4;
    }
}
