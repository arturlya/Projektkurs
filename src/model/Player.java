package model;

import com.sun.javafx.image.impl.IntArgb;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.configuration.Quality;
import de.gurkenlabs.litiengine.graphics.DebugRenderer;
import de.gurkenlabs.litiengine.graphics.IRenderable;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.StaticShadowType;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.util.TimeUtilities;
import de.gurkenlabs.litiengine.util.geom.Vector2D;
import model.Screens.IngameScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;

import static control.Timer.dt;

public abstract class Player extends GravitationalObject {

    protected double attackWindUp, attackHurtTime, attackWindDown, speed, invincibilityTimer;
    protected int directionLR, directionUD, lookingAt;
    protected Hurtbox hurtbox;
    protected boolean attackTriggered;
    protected int knockbackPercentage;

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
        setY(200);
        setX(Math.random()*300+400);

    }

    @Override
    public void render(Graphics2D g){
        super.render(g);

        if(hurtbox.isHurting()){
            g.setColor(new Color(255,0,0,100));
        }else{
            g.setColor(new Color(0,255,0,100));
        }
        g.fill(hurtbox);
        g.setColor(new Color(70,120,255));
        if(shieldActive == true) {
            g.setColor(new Color(150,150,150));
        }
        g.fill(hitbox);

    }

    @Override
    public void update() {
        super.update();

        if(invincibilityTimer > 0){
            invincibilityTimer -= dt;
        }

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
            attackTriggered = false;
        }

        //hurtbox relativ zum Spieler mitbewegen
        if(attackWindDown > 0){
            hurtbox.setRect(getX() + hurtbox.getRelativeX(), getY() + hurtbox.getRelativeY(), hurtbox.getWidth(), hurtbox.getHeight());
        }

        //bewegung, abhängig von richtung
        if(attackWindDown <= 0) {
            horizontalMovement();
        }
        horizontalDecelerate();

        if(playable) {
            //richtung setzen
            Input.keyboard().onKeyPressed(StaticData.moveLeft, (key) -> {
                directionLR = 0;
                lookingAt = 0;
                moving = true;
            });
            Input.keyboard().onKeyReleased(StaticData.moveLeft, (key) -> {
                directionLR = -1;
                moving = false;
            });
            Input.keyboard().onKeyPressed(StaticData.moveRight, (key) -> {
                directionLR = 1;
                lookingAt = 1;
            });
            Input.keyboard().onKeyReleased(StaticData.moveRight, (key) -> directionLR = -1);
            Input.keyboard().onKeyPressed(StaticData.moveUp, (key) -> directionUD = 0);
            Input.keyboard().onKeyReleased(StaticData.moveUp, (key) -> directionUD = -1);
            Input.keyboard().onKeyPressed(StaticData.moveDown, (key) -> directionUD = 1);
            Input.keyboard().onKeyReleased(StaticData.moveDown, (key) -> directionUD = -1);

            //Angriffe
            Input.keyboard().onKeyTyped(StaticData.normalAttack, (key) -> {
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
            Input.keyboard().onKeyTyped(StaticData.specialAttack, (key) -> {
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
            //Jump
            Input.keyboard().onKeyTyped(StaticData.jump, (key) -> {
                if(attackWindDown <= 0){
                    if(!inAir){
                        setVerticalSpeed(-500);
                        inAir = true;
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

    public void horizontalMovement(){
        if(directionLR == 0){
            if(horizontalSpeed > -speed){
                horizontalSpeed -= speed * dt * 40;
                moving = true;
            }
        }else if(directionLR == 1){
            if(horizontalSpeed < speed){
                horizontalSpeed += speed * dt * 40;
                moving = true;
            }
        }else if(directionLR == -1){
            moving = false;
        }
    }

    public void horizontalDecelerate(){
        if(horizontalSpeed < 50 && horizontalSpeed > -50){
            horizontalSpeed = 0;
        }else if(horizontalSpeed > 50){
            horizontalSpeed -= 1000*dt;
        }else if(horizontalSpeed < -50){
            horizontalSpeed += 1000*dt;
        }
    }

    public void registerHit(Vector2D direction, Hurtbox hurtbox){
        if(invincibilityTimer <= 0) {
            knockbackPercentage += hurtbox.getDamage();
            if(knockbackPercentage < 10){
                verticalSpeed = direction.getY() * hurtbox.getKnockback() * 1.1;
                horizontalSpeed = direction.getX() * hurtbox.getKnockback() * 1.1;
            }
            verticalSpeed = direction.getY() * hurtbox.getKnockback() * Math.pow(1.1, knockbackPercentage/10);
            horizontalSpeed = direction.getX() * hurtbox.getKnockback() * Math.pow(1.1, knockbackPercentage/10);
            //Später rausnehmen
            if(verticalSpeed != 0){
                inAir = true;
            }
            invincibilityTimer = 0.5;
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
            Game.getEnvironment().add(projectile,RenderType.NORMAL);
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
