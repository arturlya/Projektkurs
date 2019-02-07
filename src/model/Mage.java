package model;

import de.gurkenlabs.litiengine.util.geom.Vector2D;

/**
 * Klasse Warrior.
 * Erbt von der abtrakten Oberklasse Player
 */
public class Mage extends Player{

    /**Merkt sich, ob der Teleport vom Mage benutzt wurde*/
    private boolean teleUsed;

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
            hurtbox.setRelativeRect(hitbox.width,hitbox.height/4,hitbox.width,hitbox.height*0.5);
        }else{
            hurtbox.setRelativeRect(-hitbox.width,hitbox.height/4,hitbox.width,hitbox.height*0.5);
        }
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
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
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
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
        }else{
            hurtbox.setRelativeRect(-hitbox.width*1.4,hitbox.height*0.25,hitbox.width*1.4,hitbox.height*0.5);
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
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,10, new Vector2D(500,-50),5,2);
        attackWindDown = 0.3;
    }

    /**
     * Implementation der specialAttackDown vom Player.
     */
    @Override
    public void specialAttackDown() {
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
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.5;
        attackHurtTime = 0.3;
        attackWindDown  =0.4;
    }
}
