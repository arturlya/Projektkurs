package model;


import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import static control.Timer.dt;

/**
 * Klasse Warrior.
 * Erbt von der abtrakten Oberklasse Player
 */
public class Warrior extends Player{

    /** Merkt sich, ob die Folgeattacke ausgeführt werden soll bzw. der Greifhaken getroffen hat*/
    private boolean downNormalAttackActive, gettingHooked, hookingTimerSet;
    /** Cooldown des Greifhakens*/
    private double hookingTimer;

    /**
     * Konstruktor der Klasse Warrior.
     *
     * @param x X-Position des Warriors
     * @param y Y-Position des Warriors
     * @param isPlayerTester gibt an, ob der Spieler im PlayerTester oder im GameClient erstellt wurde
     */
    public Warrior(double x, double y, boolean isPlayerTester){
        super(x,y,isPlayerTester);
        setWidth(96);
        setHeight(96);
        setName("Warrior");
    }


    /**
     * Update des Interfaces IUpdateable
     */
    @Override
    public void update() {
        super.update();
        if(downNormalAttackActive && attackWindDown <= 0){
            secondDownAttack();
        }
        if(gettingHooked && !hookingTimerSet){
            hookingTimer = 0.5;
            hookingTimerSet = true;
        }
        if(hookingTimer > 0){
            hookingTimer -= dt;
        }else{
            if(gettingHooked) {
                gettingHooked = false;
                hookingTimerSet = false;
                decelerating = true;
                horizontalSpeed = horizontalSpeed / 2;
                verticalSpeed = verticalSpeed / 1.3;
            }
        }
    }

    /**
     * Hinzufügen von allen Animationen auf die HashMap von PlayerRenderer
     */
    @Override
    protected void listAllAnimations() {
        ArrayList<String> moves = pr.getAllPlayerMoves();
        moves.add("SecondDownAttack");
        super.listAllAnimations();
    }

    /**
     * Implementation der normalAttackRun vom Player.
     */
    @Override
    public void normalAttackRun() {
        //Schlag von oben nach unten
        //schaden mittel
        //knockback schwach
        if (lookingAt == 1) {
            hurtbox.setRelativeRect(hitbox.width,0,hitbox.width/2,hitbox.height);
        } else {
            hurtbox.setRelativeRect(-hitbox.width/2,0,hitbox.width/2,hitbox.height);
            pr.setAnimationOffsetX(-hitbox.width/2);
        }
        hurtbox.setDamage(5);
        hurtbox.setKnockback(3);
        attackWindUp = 0.05;
        attackHurtTime = 0.15;
        attackWindDown  =0.2;
    }
    /**
     * Implementation der normalAttackDown vom Player.
     * Auf diese Attcke folgt eine weitere Attacke 'secondDownAttack'
     */
    @Override
    public void normalAttackDown() {
        //Sticht nach vorne, danach nach hinten
        //schaden mittel
        //knockback schwach
        if(lookingAt == 0) {
            hurtbox.setRelativeRect(-hitbox.width/2, hitbox.height * 0.8, hitbox.width/2, hitbox.height * 0.2);
            pr.setAnimationOffsetX(-hitbox.width/2);
        }else{
            hurtbox.setRelativeRect(hitbox.width, hitbox.height * 0.8, hitbox.width/2, hitbox.height * 0.2);
        }
        hurtbox.setDamage(8);
        hurtbox.setKnockback(4);
        attackWindUp = 0.3;
        attackHurtTime = 0.2;
        attackWindDown = 0.05;
        downNormalAttackActive = true;
    }
    /**
     * Folgeattcke der normalAttackDown.
     */
    private void secondDownAttack() {
        if(lookingAt == 0) {
            hurtbox.setRelativeRect(hitbox.width, hitbox.height * 0.8, hitbox.width/2, hitbox.height * 0.2);
        }else{
            hurtbox.setRelativeRect(-hitbox.width/2, hitbox.height * 0.8, hitbox.width/2, hitbox.height * 0.2);
            pr.setAnimationOffsetX(-hitbox.width/2);
        }
        hurtbox.setDamage(10);
        hurtbox.setKnockback(4);
        attackWindUp = 0.05;
        attackHurtTime = 0.2;
        attackWindDown = 0.3;
        downNormalAttackActive = false;
    }
    /**
     * Implementation der normalAttackUp vom Player.
     */
    @Override
    public void normalAttackUp() {
        //sticht nach oben
        //schaden mittel
        //knockback schwach
        hurtbox.setRelativeRect(-hitbox.width*0.1, -hitbox.height*0.5,hitbox.width+hitbox.width*0.2,hitbox.height*0.5);
        pr.setAnimationOffsetX(-hitbox.width*0.1);
        pr.setAnimationOffsetY(-hitbox.height*0.5);
        hurtbox.setDamage(6);
        hurtbox.setKnockback(3);
        attackWindUp = 0.3;
        attackHurtTime = 0.3;
        attackWindDown = 0.2;
    }

    /**
     * Implementation der normalAttackStand vom Player.
     */
    @Override
    public void normalAttackStand() {
        //sticht nach vorne
        //schaden mittel
        //knockback schwach
        if(lookingAt == 1){
            hurtbox.setRelativeRect(hitbox.width,hitbox.height*0.4,hitbox.width*0.6,hitbox.getHeight()*0.2);
        }else{
            hurtbox.setRelativeRect(-hitbox.width*0.6,hitbox.height*0.4,hitbox.width*0.6,hitbox.getHeight()*0.2);
            pr.setAnimationOffsetX(-hitbox.width*0.6);
        }
        hurtbox.setDamage(7);
        hurtbox.setKnockback(2);
        attackWindUp = 0.2;
        attackHurtTime = 0.3;
        attackWindDown = 0.1;

    }
    /**
     * Implementation der specialAttackRun vom Player.
     */
    @Override
    public void specialAttackRun() {
        //schlägt mit schild
        //schaden wenig
        //knockback hoch
        if(lookingAt == 1){
            hurtbox.setRelativeRect(hitbox.width,hitbox.height*0.2,hitbox.width*0.7,hitbox.height*0.6);
        }else{
            hurtbox.setRelativeRect(-hitbox.width*0.7,hitbox.height*0.2,hitbox.width*0.7,hitbox.height*0.6);
            pr.setAnimationOffsetX(-hitbox.width*0.7);
        }
        hurtbox.setDamage(2);
        hurtbox.setKnockback(8);
        attackWindUp = 0.1;
        attackHurtTime = 0.2;
        attackWindDown = 0.4;
    }
    /**
     * Implementation der specialAttackDown vom Player.
     */
    @Override
    public void specialAttackDown() {
        //blockt
        attackWindUp = 0.5;
        shieldActive = true;
        attackWindDown = 0.3;
    }
    /**
     * Implementation der specialAttackUp vom Player.
     */
    @Override
    public void specialAttackUp() {
        //schießt haken
        //schaden schwach
        if(projectile == null) {
            shootGrapplingHook();
        }
    }
    /**
     * Implementation der specialAttackStand vom Player.
     */
    @Override
    public void specialAttackStand() {
        //wirft messer
        //schaden schwach
        //knockback schwach
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,10,null,new Vector2D(1000,0),3,1);
        attackWindDown = 0.3;
    }

    /** Schießt einen Greifhaken*/
    protected void shootGrapplingHook(){
        projectile = new GrapplingHook(this,getX(),getY());
        Game.getEnvironment().add(projectile);
        Game.getEnvironment().add(projectile, RenderType.NORMAL);
    }

    /** Zieht den Warrior zum Greifhaken hin*/
    public void pullToHook(){
        if(projectile != null) {
            gettingHooked = true;
            horizontalSpeed = (projectile.getX() - getX()) * 2;
            verticalSpeed = (projectile.getY() - getY()) * 2;
        }
    }

    /**
     * Klasse GrapplingHook, dem Rettungsprojektil des Warriors
     */
    public class GrapplingHook extends Projectile{

        /** Ob die Richtung des Greifhakens bestimmt ist*/
        boolean directionChosen;
        /** Ob der Greifhaken aktiv ist*/
        boolean isActive;
        /** Besitzer des Greifhakens*/
        Warrior owner;

        /**
         * Konstruktor der Klasse GrapplingHook
         *
         * @param owner Erzeuger des GrapplingHooks
         * @param x X-Position des GrapplingHooks
         * @param y Y-Position des GrapplingHooks
         */
        public GrapplingHook(Warrior owner, double x, double y){
            super(owner,x,y,5,5, null, new Vector2D(400,-750),3,0);
            this.owner = owner;
            hitbox = new Rectangle2D.Double(getX(),getY(),getWidth(),getHeight());
            isActive = false;
            directionChosen = false;
            inAir = true;
        }
    }

    /**
     * @param hookingTimer Setzt den hookingTimer auf diesen Wert.
     */
    public void setHookingTimer(double hookingTimer) {
        this.hookingTimer = hookingTimer;
    }

    /** @return Gibt zurück, ob der Greifhaken etwas getroffen hat*/
    public boolean isGettingHooked() {
        return gettingHooked;
    }

    /** @param gettingHooked Setzt gettingHooked auf diesen Wert*/
    public void setGettingHooked(boolean gettingHooked) {
        this.gettingHooked = gettingHooked;
    }
}
