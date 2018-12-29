package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;
import view.PlayerRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import static control.Timer.dt;

public abstract class Player extends GravitationalObject {

    protected double attackWindUp, attackHurtTime, attackWindDown, maxSpeed, speed, invincibilityTimer, jumpCooldown;
    protected int directionLR, directionUD, lookingAt, jumpsAvailable;
    protected Hurtbox hurtbox;
    protected boolean attackTriggered, attacking;
    protected int knockbackPercentage;

    protected boolean shieldActive;
    protected Projectile projectile;
    protected boolean moving;
    protected int playerNumber;
    protected boolean playable;

    protected boolean renderHurtboxes = true;
    protected Rectangle2D renderHurtbox;
    protected double rx, ry, rwidth, rheight, offScreenX, offScreenY, offScreenWidth, offScreenHeight;
    protected Point corner;
    protected Image[] offScreenCircles;
    protected Image activeOffScreenCircle;
    protected double circleX, circleY;

    /**
     * Konstruktor der abstrakten Klasse Player
     * @param playable entscheided, ob der Player spielbar, also von Tastatur-Inputs kontrolliert wird
     */
    public Player(double x, double y, boolean playable){
        super();
        hitbox = new Rectangle2D.Double(0,0,96,96);
        hurtbox = new Hurtbox(50,50,50,50);
        directionLR = -1;
        directionUD = -1;
        maxSpeed = 300;
        speed = 4000;
        this.playable = playable;
        setX(x);
        setY(y);
        //setY(200);
        //setX(Math.random()*300+400);
        jumpsAvailable = 2;
        renderHurtbox = new Rectangle2D.Double(0,0,0,0);
        corner = new Point(0,0);
        //createCircleImages();
        PlayerRenderer pr = new PlayerRenderer(this);
        Game.getEnvironment().add(pr,RenderType.NORMAL);
        Game.getLoop().attach(pr);

    }

    /*private void createCircleImages(){
        offScreenCircles = new Image[8];
        try {
            offScreenCircles[0] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleDown.png"));
            offScreenCircles[1] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleLeftDown.png"));
            offScreenCircles[2] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleLeft.png"));
            offScreenCircles[3] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleLeftUp.png"));
            offScreenCircles[4] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleUp.png"));
            offScreenCircles[5] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleRightUp.png"));
            offScreenCircles[6] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleRight.png"));
            offScreenCircles[7] = ImageIO.read(new File("assets/img/ingame/offScreenCircles/circleRightDown.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        activeOffScreenCircle = offScreenCircles[0];
    }*/
/*
    /**
     * Methode, um den Spieler graphisch darzustellen
     * @param g übergebene Graphics2D, um das Zeichnen zu ermöglichen
     */
    @Override
    public void render(Graphics2D g){
        /*super.render(g);
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
        if(!hitbox.intersects(0,0,1920,1080)){
            g.drawImage(activeOffScreenCircle, (int)(circleX/1920*gameWidth), (int)(circleY/1080*gameHeight), (int)(150.0/1920*gameWidth), (int)(150.0/1080*gameHeight), null);
        }*/
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
        //adjustRenderHitbox();
        if (!inAir) {
            jumpsAvailable = 2;
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
        if(attackWindDown > 0){
            attacking = true;
        }else{
            attacking = false;
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
            if (!projectile.getHitbox().intersects(new Rectangle(0,0,1920,1080))) {
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
        if(attackWindDown <= 0 || inAir) {
            if (directionLR == 0) {
                if (horizontalSpeed > -maxSpeed) {
                    horizontalSpeed -= dt * speed;
                    moving = true;
                }
            } else if (directionLR == 1) {
                if (horizontalSpeed < maxSpeed) {
                    horizontalSpeed += dt * speed;
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
            attackWindUp = 0;
            attackHurtTime = 0;
            attackWindDown = 0;
        }
    }
/*
    /**
     * Methode, die die RenderHurtbox auf die Größe des Fensters anpasst
     *//*
    private void adjustRenderHitbox(){
        rx = hitbox.x/1920*gameWidth;
        ry = hitbox.y/1080*gameHeight;
        rwidth = hitbox.width/1920*gameWidth;
        rheight = hitbox.height/1080*gameHeight;
        if(!hitbox.intersects(0,0,1920,1080)){
            double factor = 0;
            if(hitbox.intersects(-200,-200,200,200) || hitbox.intersects(1920,-200,200,200) || hitbox.intersects(-200,1080,200,200) || hitbox.intersects(1920,1080,200,200)) {
                if(hitbox.intersects(-200,-200,200,200)){
                    circleX = 0;
                    circleY = 0;
                    activeOffScreenCircle = offScreenCircles[3];
                    offScreenX = 75.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 75.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(0,0);
                }else if(hitbox.intersects(1920,-200,200,200)){
                    circleX = 1770;
                    circleY = 0;
                    activeOffScreenCircle = offScreenCircles[5];
                    offScreenX = 1845.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 75.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(1920,0);
                }else if(hitbox.intersects(-200,1080,200,200)){
                    circleX = 0;
                    circleY = 930;
                    activeOffScreenCircle = offScreenCircles[1];
                    offScreenX = 75.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 1005.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(0,1080);
                }else if(hitbox.intersects(1920,1080,200,200)){
                    circleX = 1770;
                    circleY = 930;
                    activeOffScreenCircle = offScreenCircles[7];
                    offScreenX = 1845.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = 1005.0/1080*gameHeight - offScreenHeight * 0.5;
                    corner.setLocation(1920,1080);
                }
                factor = corner.distance(getX(),getY()) * -0.0029 + 1.32;
            }else{
                if(hitbox.intersects(0,-200,1920,200)){
                    circleX = getX() - 50;
                    circleY = 0;
                    activeOffScreenCircle = offScreenCircles[4];
                    offScreenX = rx + 0.5 * rwidth - offScreenWidth * 0.5;
                    offScreenY = 75.0/1080*gameHeight - offScreenHeight * 0.5;
                    factor = Math.abs(getY()) * -0.005 + 1.5;
                }else if(hitbox.intersects(0,1080,1920,200)){
                    circleX = getX() - 50;
                    circleY = 930;
                    activeOffScreenCircle = offScreenCircles[0];
                    offScreenX = rx + 0.5 * rwidth - offScreenWidth * 0.5;
                    offScreenY = 1005.0/1080*gameHeight - offScreenHeight * 0.5;
                    factor = (getY()-1080) * -0.005 + 1.5;
                }else if(hitbox.intersects(-200,0,200,1080)){
                    circleX = 0;
                    circleY = getY() - 25;
                    activeOffScreenCircle = offScreenCircles[2];
                    offScreenX = 75.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = ry + 0.5 * rheight - offScreenHeight * 0.5;
                    factor = (Math.abs(getX())) * -0.005 + 1.5;
                }else if(hitbox.intersects(1920,0,200,1080)){
                    circleX = 1770;
                    circleY = getY() - 25;
                    activeOffScreenCircle = offScreenCircles[6];
                    offScreenX = 1845.0/1920*gameWidth - offScreenWidth * 0.5;
                    offScreenY = ry + 0.5 * rheight - offScreenHeight * 0.5;
                    factor = (Math.abs(getX()-1920)) * -0.005 + 1.5;
                }
            }
            offScreenWidth = rwidth * factor;
            offScreenHeight = rheight * factor;
            renderHitbox.setRect(offScreenX, offScreenY, offScreenWidth, offScreenHeight);
        }
    }*/

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

    public boolean isAttacking() {
        return attacking;
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
