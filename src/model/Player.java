package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.util.geom.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static control.Timer.dt;

public abstract class Player extends GravitationalObject {

    protected double attackWindUp, attackHurtTime, attackWindDown, speed, invincibilityTimer, jumpCooldown;
    protected int directionLR, directionUD, lookingAt, jumpsAvailable;
    protected Hurtbox hurtbox;
    protected boolean attackTriggered;
    protected int knockbackPercentage;

    protected boolean shieldActive;
    protected Projectile projectile;
    protected boolean moving;
    protected int playerNumber;
    protected boolean playable;

    protected boolean renderHurtboxes = true;
    protected Rectangle2D renderHurtbox;

    /**
     * Konstruktor der abstrakten Klasse Player
     * @param playable entscheided, ob der Player spielbar, also von Tastatur-Inputs kontrolliert wird
     */
    public Player(double x, double y, boolean playable){
        super();
        hitbox = new Rectangle2D.Double(0,0,50,100);
        hurtbox = new Hurtbox(50,50,50,50);
        directionLR = -1;
        directionUD = -1;
        speed = 100;
        this.playable = playable;
        setX(x);
        setY(y);
        //setY(200);
        //setX(Math.random()*300+400);
        jumpsAvailable = 2;
        renderHurtbox = new Rectangle2D.Double(0,0,0,0);
    }

    /**
     * Methode, um den Spieler graphisch darzustellen
     * @param g übergebene Graphics2D, um das Zeichnen zu ermöglichen
     */
    @Override
    public void render(Graphics2D g){
        super.render(g);
        if(renderHurtboxes) {

            if (hurtbox.isHurting()) {
                g.setColor(new Color(255, 0, 0, 100));
            } else {
                g.setColor(new Color(0, 255, 0, 100));
            }

            g.fill(renderHurtbox);
        }
        g.setColor(new Color(70,120,255));
        if(shieldActive) {
            g.setColor(new Color(150,150,150));
        }
        g.fill(renderHitbox);

    }

    /**
     * Diese Methode wird mehrmals pro Sekunde aufgerufen um andere Update-Methoden aufzurufen
     */
    @Override
    public void update() {
        super.update();
        regulateTimers();
        setShapes();
        horizontalMovement();
        horizontalDecelerate();
       // processInputs();
        removeProjectiles();
        if(!inAir){
            jumpsAvailable = 2;
        }
        if(renderHurtboxes){
            renderHurtbox.setRect(hurtbox.x/1920*gameWidth, hurtbox.y/1080*gameHeight, hurtbox.width/1920*gameWidth, hurtbox.height/1080*gameHeight);
        }
    }

    /**
     * Methode um Attack- und Invincibility-Timer herunter zu zählen
     */
    private void regulateTimers(){
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
        if(invincibilityTimer > 0){
            invincibilityTimer -= dt;
        }
        if(jumpCooldown > 0){
            jumpCooldown -= dt;
        }
    }

    /**
     * Methode, um HurtBox zu den Koordinaten des Players zu setzen
     */
    private void setShapes(){
        if(attackWindDown <= 0){
            hurtbox.setRect(-100,-100,0,0);
        }

        if(attackWindDown > 0){
            hurtbox.setRect(getX() + hurtbox.getRelativeX(), getY() + hurtbox.getRelativeY(), hurtbox.getWidth(), hurtbox.getHeight());
        }
    }

    /**
     * Methode, um Projektile zu entfernen, falls sie außerhalb des Screens sind
     */
    private void removeProjectiles(){
        if (projectile != null) {
            if (!projectile.getHitbox().intersects(Game.getScreenManager().getBounds())) {
                Game.getEnvironment().remove(projectile);
                Game.getEnvironment().removeRenderable(projectile);
                projectile = null;
            }
        }
    }

    /**
     * Methode, die die horizontale Bewegung durchführt
     */
    private void horizontalMovement(){
        if(attackWindDown <= 0) {
            if (directionLR == 0) {
                if (horizontalSpeed > -speed) {
                    horizontalSpeed -= speed * dt * 40;
                    moving = true;
                }
            } else if (directionLR == 1) {
                if (horizontalSpeed < speed) {
                    horizontalSpeed += speed * dt * 40;
                    moving = true;
                }
            } else if (directionLR == -1) {
                moving = false;
            }
        }
    }

    /**
     * Methode, die den Player horizontal abbremst
     */
    private void horizontalDecelerate(){
        if(horizontalSpeed < 50 && horizontalSpeed > -50){
            horizontalSpeed = 0;
        }else if(horizontalSpeed > 50){
            horizontalSpeed -= 1000*dt;
        }else if(horizontalSpeed < -50){
            horizontalSpeed += 1000*dt;
        }
    }

    /**
     * Methode, die den Player zurückschlagen, wenn er von einem Angriff getroffen wird
     * @param direction Vector, der die Richtung des Schlages angibt
     * @param hurtbox HurtBox, von dem der Player getroffen wird
     */
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
                moving = true;
            }else{
                moving = false;
            }
            invincibilityTimer = 0.5;
        }
    }

    /**
     * Methode, die ein Projektil erstellt
     * @param paramX X-Koordinate des Projektils
     * @param paramY Y- Koordinate des Projektils
     * @param width Breite des Projektils
     * @param height Höhe des Projektils
     */
    public void shoot(double paramX,double paramY,int width,int height) {
        if (projectile == null){
            projectile = new Projectile(this, paramX, paramY, width, height);
            Game.getEnvironment().add(projectile);
            Game.getEnvironment().add(projectile,RenderType.NORMAL);
        }
    }

    public void spawn(Point point){
        knockbackPercentage = 0;
        attackWindUp = 0;
        attackHurtTime = 0;
        attackWindDown = 0;
        setVerticalSpeed(0);
        this.setX(point.x);
        this.setY(point.y);
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

    public boolean isMoving() {
        if(!inAir) {
            return moving;
        }else{
            return true;
        }
    }

    public void setPlayerNumber(int value){
        playerNumber = value;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }
    public void setShieldActive(boolean state){shieldActive = state;}

    public Projectile getProjectile(){return projectile;}



    public void setDirectionLR(int directionLR) {
        this.directionLR = directionLR;
    }

    public void setLookingAt(int lookingAt) {
        this.lookingAt = lookingAt;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setDirectionUD(int directionUD) {
        this.directionUD = directionUD;
    }

    public int getJumpsAvailable() {
        return jumpsAvailable;
    }

    public void setJumpsAvailable(int jumpsAvailable) {
        this.jumpsAvailable = jumpsAvailable;
    }

    public double getJumpCooldown() {
        return jumpCooldown;
    }

    public void setJumpCooldown(double jumpCooldown) {
        this.jumpCooldown = jumpCooldown;
    }

    public double getAttackWindDown() {
        return attackWindDown;
    }

    public int getDirectionLR() {
        return directionLR;
    }

    public int getDirectionUD() {
        return directionUD;
    }
}
