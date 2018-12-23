package model;


import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;

import java.awt.geom.Rectangle2D;

public class Warrior extends Player{

    private boolean downNormalAttackActive;
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
        if(downNormalAttackActive && attackWindDown <= 0){
            secondDownAttack();
        }
    }

    @Override
    public void normalAttackRun() {
        //Schlag von oben nach unten
        //schaden mittel
        //knockback schwach
        if(!inAir) {
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
        }
    }

    @Override
    public void normalAttackDown() {
        //Sticht nach vorne, danach nach hinten
        //schaden mittel
        //knockback schach
        if(!inAir){
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
            downNormalAttackActive = true;
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
        downNormalAttackActive = false;
    }

    @Override
    public void normalAttackUp() {
        //sticht nach oben
        //schaden mittel
        //knockback schwach
        if(!inAir){
            hurtbox.setRelativeRect(-hitbox.width*0.1, -hitbox.height*0.5,hitbox.width+hitbox.width*0.2,hitbox.height*0.5);
            hurtbox.setDamage(0);
            hurtbox.setKnockback(0);
            attackWindUp = 0.3;
            attackHurtTime = 0.3;
            attackWindDown = 0.2;
        }
    }

    @Override
    public void normalAttackStand() {
        //sticht nach vorne
        //schaden mittel
        //knockback schwach
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
        //schlägt mit schild
        //schaden wenig
        //knockback hoch
    }

    @Override
    public void specialAttackDown() {
        //blockt
        if(!inAir){
            attackWindUp = 0.5;
            shieldActive = true;
            attackWindDown = 1;
        }
    }

    @Override
    public void specialAttackUp() {
        //schießt haken
        //schaden schwach
        if(hook == null) {
            shootGrapplingHook();
        }
    }

    @Override
    public void specialAttackStand() {
        //wirft messer
        //schaden schach
        //knockback schwach
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,10);
        attackWindDown = 0.3;
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
