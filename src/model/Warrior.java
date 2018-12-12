package model;


import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.Screens.IngameScreen;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Warrior extends Player{

    private boolean firstAttackDown;
    private GrapplingHook hook;

    public Warrior(double x, double y, boolean playable){
        super(x,y,playable);
        setWidth(50);
        setHeight(100);
        setName("Warrior");
    }


    @Override
    public void update() {
        super.update();
        if(hook != null){
            if(!hook.getHitbox().intersects(Game.getScreenManager().getBounds())){
                Game.getEnvironment().removeRenderable(hook);
                Game.getEnvironment().remove(hook);
                hook = null;
            }
        }
        if(firstAttackDown){
            secondDownAttack();
        }
    }

    @Override
    public void normalAttackRun() {
        if(!inAir) {
            nAR = true;
            directionLR = -1;
            if (lookingAt == 1) {
                hurtbox.setRelativeRect(hitbox.width,0,hitbox.width,hitbox.height);


            } else {
                hurtbox.setRelativeRect(-hitbox.width,0,hitbox.width,hitbox.height);
            }
            hurtbox.setDamage(0);
            hurtbox.setKnockback(0);
            attackWindUp = 0.1;
            attackHurtTime = 0.3;
            attackWindDown  =0.2;
            nAR = false;
        }
    }

    @Override
    public void normalAttackDown() {
        if(!inAir){
            nAD = true;
            if(lookingAt == 0) {
                hurtbox.setRelativeRect(-hitbox.width, hitbox.height * 0.8, hitbox.width, hitbox.height * 0.2);
            }else{
                hurtbox.setRelativeRect(hitbox.width, hitbox.height * 0.8, hitbox.width, hitbox.height * 0.2);
            }
            hurtbox.setDamage(0);
            hurtbox.setKnockback(0);
            attackWindUp = 0.5;
            attackHurtTime = 0.3;
            attackWindDown = 0.2;
            if(!hurtbox.isHurting()) {
                firstAttackDown = true;
            }
        }
    }

    private void secondDownAttack() {
        if(lookingAt == 0) {
            hurtbox.setRelativeRect(hitbox.width, hitbox.height * 0.8, hitbox.width, hitbox.height * 0.2);
        }else{
            hurtbox.setRelativeRect(-hitbox.width, hitbox.height * 0.8, hitbox.width, hitbox.height * 0.2);
        }
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.1;
        attackHurtTime = 0.3;
        attackWindDown = 0.8;
        firstAttackDown = false;
        nAD = false;
    }

    @Override
    public void normalAttackUp() {
        if(!inAir){
            nAU = true;
            hurtbox.setRelativeRect(-hitbox.width*0.1, -hitbox.height*0.5,hitbox.width+hitbox.width*0.2,hitbox.height*0.5);
            hurtbox.setDamage(0);
            hurtbox.setKnockback(0);
            attackWindUp = 0.3;
            attackHurtTime = 0.3;
            attackWindDown = 0.2;
            nAU = false;
        }
    }

    @Override
    public void normalAttackStand() {
        nAS = true;
        if(lookingAt == 1){
            hurtbox.setRelativeRect(hitbox.width,hitbox.height*0.4,hitbox.width+hitbox.width*0.5,hitbox.getHeight()*0.2);
            hurtbox.setDamage(0);
            hurtbox.setKnockback(0);
            attackWindUp = 0.2;
            attackHurtTime = 0.3;
            attackWindDown = 0.1;
        }else{
            hurtbox.setRelativeRect(-hitbox.width*1.5,hitbox.height*0.4,hitbox.width+hitbox.width*0.5,hitbox.getHeight()*0.2);
            hurtbox.setDamage(0);
            hurtbox.setKnockback(0);
            attackWindUp = 0.2;
            attackHurtTime = 0.3;
            attackWindDown = 0.1;
        }

    }

    @Override
    public void specialAttackRun() {
        sAR  =true;
        sAR = false;
    }

    @Override
    public void specialAttackDown() {
        if(!inAir){
            sAD = true;
            attackWindUp = 0.5;
            shieldActive = true;
            attackWindDown = 1;
            sAD = false;
        }
    }

    @Override
    public void specialAttackUp() {
        if(hook == null) {
            sAU = true;
            shootGrapplingHook();
            sAU = false;
        }
    }

    @Override
    public void specialAttackStand() {
        sAS = true;
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,10);
        attackWindDown = 0.3;
        sAS = false;
    }

    protected void shootGrapplingHook(){
        hook = new GrapplingHook(this,getX(),getY());
        Game.getEnvironment().add(hook);
        Game.getEnvironment().add(hook, RenderType.NORMAL);
    }

    private class GrapplingHook extends GravitationalObject{

        boolean directionChosen;
        boolean isActive;
        Warrior owner;

        public GrapplingHook(Warrior owner, double x, double y){
            this.owner = owner;
            setX(x);
            setY(y);
            setWidth(5);
            setHeight(5);
            hitbox = new Rectangle2D.Double(getX(),getY(),getWidth(),getHeight());
            isActive = false;
            directionChosen = false;
            inAir = true;
            setVerticalSpeed(-350);
        }

        @Override
        public void update(){
            super.update();
            if(!directionChosen) {
                if (owner.getLookingAt() == 1) {
                    setHorizontalSpeed(700);
                } else {
                    setHorizontalSpeed(-700);
                }
                directionChosen = true;
            }
        }
    }
}
