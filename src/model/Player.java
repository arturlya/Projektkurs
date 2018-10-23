package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import static control.Timer.dt;

public abstract class Player extends GravitationalObject{

    protected double attackWindUp, attackHurtTime, attackWindDown, speed;
    protected int directionLR, directionUD, lookingAt;
    protected Hurtbox hurtbox;
    protected boolean shieldActive;
    protected Projectile projectile;
    protected boolean moving;
    protected int playerNumber;
    protected boolean playable;

    public Player(boolean playable){
        hitbox = new Rectangle2D.Double(0,0,50,100);
        hurtbox = new Hurtbox(50,50,50,50);
        directionLR = -1;
        directionUD = -1;
        speed = 100;
        this.playable = playable;
    }

    @Override
    public void update() {
        super.update();


        if(attackWindDown <= 0){
            hurtbox.setRect(-100,-100,0,0);
        }

        //hurtbox bei angriffen, angrifftimer
        if(attackWindUp > 0){
            hurtbox.setHurting(false);
            attackWindUp -= dt;
        }else if(attackHurtTime > 0){
            hurtbox.setHurting(true);
            attackHurtTime -= dt;
        }else if(attackWindDown > 0){
            hurtbox.setHurting(false);
            attackWindDown -= dt;
        }
        if(attackWindUp <= 0){
            shieldActive = false;
        }

        //bewegung, abhängig von richtung
        if(attackWindDown <= 0) {
            switch (directionLR) {
                case 0:
                    setHorizontalSpeed(-speed);
                    moving = true;
                    break;
                case 1:
                    setHorizontalSpeed(speed);
                    moving = true;
                    break;
                case -1:
                    setHorizontalSpeed(0);
                    moving = false;
                    break;
            }
        }

        if(playable) {
            //richtung setzen
            Input.keyboard().onKeyPressed(KeyEvent.VK_LEFT, (key) -> {
                directionLR = 0;
                lookingAt = 0;
                moving = true;
            });
            Input.keyboard().onKeyReleased(KeyEvent.VK_LEFT, (key) -> {
                directionLR = -1;
                moving = false;
            });
            Input.keyboard().onKeyPressed(KeyEvent.VK_RIGHT, (key) -> {
                directionLR = 1;
                lookingAt = 1;
            });
            Input.keyboard().onKeyReleased(KeyEvent.VK_RIGHT, (key) -> directionLR = -1);
            Input.keyboard().onKeyPressed(KeyEvent.VK_UP, (key) -> directionUD = 0);
            Input.keyboard().onKeyReleased(KeyEvent.VK_UP, (key) -> directionUD = -1);
            Input.keyboard().onKeyPressed(KeyEvent.VK_DOWN, (key) -> directionUD = 1);
            Input.keyboard().onKeyReleased(KeyEvent.VK_DOWN, (key) -> directionUD = -1);

            //Angriffe
            Input.keyboard().onKeyTyped(KeyEvent.VK_A, (key) -> {
                if (attackWindDown <= 0) {
                    setHorizontalSpeed(0);
                    if (directionLR != -1) {
                        normalAttackRun();
                    } else if (directionUD == 1) {
                        normalAttackDown();
                    } else if (directionUD == 0) {
                        normalAttackUp();
                    } else {
                        normalAttackStand();
                    }
                }
            });
            Input.keyboard().onKeyTyped(KeyEvent.VK_S, (key) -> {
                if (attackWindDown <= 0) {
                    setHorizontalSpeed(0);
                    if (directionLR != -1) {
                        specialAttackRun();
                    } else if (directionUD == 1) {
                        specialAttackDown();
                    } else if (directionUD == 0) {
                        specialAttackUp();
                    } else {
                        specialAttackStand();
                    }
                }
            });
        }
        if (projectile != null) {
            if (!projectile.getHitbox().intersects(Game.getScreenManager().getBounds())) {
                ((IngameScreen) Game.getScreenManager().getCurrentScreen()).removeGravObject(projectile);
                Game.getEnvironment().remove(projectile);
                projectile = null;
            }
        }
    }

    public abstract void normalAttackRun();

    public abstract void normalAttackDown();

    public abstract void normalAttackUp();

    public abstract void normalAttackStand();

    public abstract void specialAttackRun();

    public abstract void specialAttackDown();

    public abstract void specialAttackUp();

    public abstract void specialAttackStand();

    public Hurtbox getHurtbox() {
        return hurtbox;
    }

    public void setHurtbox(Hurtbox hurtbox) {
        this.hurtbox = hurtbox;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public int getLookingAt() {
        return lookingAt;
    }

    public void shoot(double paramX,double paramY,int width,int height) {
        if (projectile == null){
            projectile = new Projectile(this, paramX, paramY, width, height);
            ((IngameScreen) Game.getScreenManager().getCurrentScreen()).addGravObject(projectile);
            Game.getEnvironment().add(projectile);
        }
    }

    public boolean isMoving() {
        return moving;
    }
    public void setPlayerNumber(int value){
        playerNumber = value;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }
}
