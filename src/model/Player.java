package model;

import control.ScreenController;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;
import view.PlayerRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;


import static control.Timer.dt;

/**
 * Abstrakte Oberklasse Player.
 *
 */
public abstract class Player extends GravitationalObject {

    /** Speichert wie lange eine Attacke zum ausführen braucht, die Geschwindigkeit, und wann der Spieler wieder springen kann*/
    protected double attackWindUp, attackHurtTime, attackWindDown, maxSpeed, speed, invincibilityTimer, jumpCooldown;
    /** Speichert in welche Richtung der Spieler sich bewegt, guckt und wie oft er noch springen kann*/
    protected int directionLR, directionUD, lookingAt, jumpsAvailable;
    /** Speichert die Schlag-Hurtbox des Spielers*/
    protected Hurtbox hurtbox;
    /** Merkt sich, ob eine Attacke wahrgenommen und ausgeführt wird*/
    protected boolean attackTriggered, attacking;
    /** Speichert den Rückstoß des Spielers*/
    protected int knockbackPercentage;

    /** Speichert, ob der Schild momentan aktiv ist*/
    protected boolean shieldActive;
    /** Speichert das Projektil des Spielers*/
    protected Projectile projectile;
    /** Merkt sich, ob der Spieler sich bewegt oder entschleunigt*/
    protected boolean moving, decelerating;
    /** Speichert die Spielernummer des Spielers auf dem Server*/
    protected int playerNumber;
    /** Speichert, wie viele Leben der Spieler hat*/
    protected int stocks;

    /** Visualisierung des Spielers*/
    protected PlayerRenderer pr;

    /**
     * Konstruktor der abstrakten Klasse Player
     * @param isPlayerTester gibt an, ob der Spieler im PlayerTester oder im GameClient erstellt wurde
     */
    public Player(double x, double y, boolean isPlayerTester){
        super();
        hitbox = new Rectangle2D.Double(0,0,96,96);
        hurtbox = new Hurtbox(50,50,50,50);
        directionLR = -1;
        directionUD = -1;
        maxSpeed = 300;
        speed = 4000;
        setX(x);
        setY(y);
        jumpsAvailable = 2;
        stocks = 3;
        pr = new PlayerRenderer(this);
        if(!isPlayerTester) {
            ScreenController.environments.get(1).add(pr, RenderType.NORMAL);
        }else {
            Game.getEnvironment().add(pr, RenderType.NORMAL);
        }
        Game.getLoop().attach(pr);

    }


    /**
     * Diese Methode wird mehrmals pro Sekunde aufgerufen um andere Update-Methoden aufzurufen
     */
    @Override
    public void update() {
        super.update();
        regulateTimers();
        setShapes();
        if(!(this instanceof Warrior && (((Warrior) this).isGettingHooked()))) {
            horizontalMovement();
            horizontalDecelerate();
        }
        removeProjectiles();
        if (!inAir) {
            jumpsAvailable = 2;
        }
        if(attacking){
            decelerating = true;
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
        if(!attacking){
            hurtbox.setRect(-100,-100,0,0);
        }

        if(attacking){
            hurtbox.setRect(getX() + hurtbox.getRelativeX(), getY() + hurtbox.getRelativeY(), hurtbox.getWidth(), hurtbox.getHeight());
        }
    }

    /**
     * Methode, um Projektile zu entfernen, falls sie außerhalb des Screens sind
     */
    public void removeProjectiles(){
        if (projectile != null) {
            if (!projectile.getHitbox().intersects(new Rectangle(-200,-200,2320,1480))) {
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
        if((!attacking || inAir) && !decelerating) {
            if (directionLR == 0) {
                if (horizontalSpeed > -maxSpeed) {
                    if(!inAir) {
                        horizontalSpeed -= dt * speed;
                    }else{
                        horizontalSpeed -= dt * speed / 3;
                    }
                    moving = true;
                }
            } else if (directionLR == 1) {
                if (horizontalSpeed < maxSpeed) {
                    if(!inAir) {
                        horizontalSpeed += dt * speed;
                    }else{
                        horizontalSpeed += dt * speed / 3;
                    }
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
        if(decelerating) {
            if (horizontalSpeed < 15 && horizontalSpeed > -15) {
                horizontalSpeed = 0;
                directionLR = -1;
                decelerating = false;
            } else if (horizontalSpeed >= 15) {
                if (!inAir) {
                    horizontalSpeed -= 1000 * dt;
                } else {
                    horizontalSpeed -= 300 * dt;
                }
            } else if (horizontalSpeed <= -15) {
                if (!inAir) {
                    horizontalSpeed += 1000 * dt;
                } else {
                    horizontalSpeed += 300 * dt;
                }
            }
        }
    }

    /**
     * Methode, die den Player zurückschlagen, wenn er von einem Angriff getroffen wird
     * @param direction Vector, der die Richtung des Schlages angibt
     */
    public void registerHit(Vector2D direction, int damage, int knockback){
        if(invincibilityTimer <= 0) {
            knockbackPercentage += damage;
            if(knockbackPercentage < 10){
                verticalSpeed = direction.getY() * knockback * 1.1;
                horizontalSpeed = direction.getX() * knockback * 1.1;
            }
            verticalSpeed = direction.getY() * knockback * Math.pow(1.1, knockbackPercentage/10);
            horizontalSpeed = direction.getX() * knockback * Math.pow(1.1, knockbackPercentage/10);
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
            decelerating = true;
        }
    }


    /**
     * Methode, die ein Projektil erstellt
     * @param paramX X-Koordinate des Projektils
     * @param paramY Y- Koordinate des Projektils
     * @param width Breite des Projektils
     * @param height Höhe des Projektils
     */
    public void shoot(double paramX,double paramY,double width,double height, Vector2D direction, int damage, int knockback) {
        if (projectile == null){
            projectile = new Projectile(this, paramX, paramY, (int)width, (int)height, null, direction, damage, knockback);
            Game.getEnvironment().add(projectile);
            Game.getEnvironment().add(projectile,RenderType.NORMAL);
        }
    }

    /**
     * Setzt den Spieler an einen Punkt
     *
     * @param point Spawnpoint des Spielers
     */
    public void spawn(Point point){
        knockbackPercentage = 0;
        attackWindUp = 0;
        attackHurtTime = 0;
        attackWindDown = 0;
        setVerticalSpeed(0);
        this.setX(point.x);
        this.setY(point.y);
    }

    /** Abstrakte Attacke*/
    public abstract void normalAttackRun();

    /** Abstrakte Attacke*/
    public abstract void normalAttackDown();

    /** Abstrakte Attacke*/
    public abstract void normalAttackUp();

    /** Abstrakte Attacke*/
    public abstract void normalAttackStand();

    /** Abstrakte Attacke*/
    public abstract void specialAttackRun();

    /** Abstrakte Attacke*/
    public abstract void specialAttackDown();

    /** Abstrakte Attacke*/
    public abstract void specialAttackUp();

    /** Abstrakte Attacke*/
    public abstract void specialAttackStand();

    /**@return Gibt zurück, ob der Spieler entschleunigt*/
    public boolean isDecelerating() {
        return decelerating;
    }

    /**@param decelerating setzt decelerating auf diesen Wert*/
    public void setDecelerating(boolean decelerating) {
        this.decelerating = decelerating;
    }

    /**@return Gibt die Hurtbox des Spielers zurück*/
    public Hurtbox getHurtbox() {
        return hurtbox;
    }

    /**@return Gibt zurück, ob der Spieler angreift*/
    public boolean isAttacking() {
        return attacking;
    }

    /**@return Gibt den Knockback Wert zurück*/
    public int getKnockbackPercentage() {
        return knockbackPercentage;
    }

    /**@return Gibt zurück, ob der Schild des Spielers aktiv ist*/
    public boolean isShieldActive() {
        return shieldActive;
    }

    /**@return Gibt zurück, ob wohin der Spieler schaut*/
    public int getLookingAt() {
        return lookingAt;
    }

    /**@param projectile Setzt das Projektil des Spielers auf dieses Projektil*/
    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }


    /**@return Gibt zurück, ob der Spieler sich bewegt*/
    public boolean isMoving() {
        if(!inAir) {
            return moving;
        }else{
            return true;
        }
    }

    /**@param value Setzt die Spielernummer auf diese Zahl*/
    public void setPlayerNumber(int value){
        playerNumber = value;
    }

    /**@return Gibt die Spielernummer zurück*/
    public int getPlayerNumber(){
        return playerNumber;
    }

    /**@param state Setzt den Schild auf diesen Zustand*/
    public void setShieldActive(boolean state){shieldActive = state;}

    /**@return Gibt das Projektil des Spielers zurück */
    public Projectile getProjectile(){return projectile;}


    /**@param directionLR  Setzt die horizontale Richtung des Spielers auf diesen Wert*/
    public void setDirectionLR(int directionLR) {
        this.directionLR = directionLR;
    }

    /**@param lookingAt Lässt den Spieler in diese Richtung schauen*/
    public void setLookingAt(int lookingAt) {
        this.lookingAt = lookingAt;
    }

    /**@param moving Setzt den Status, ob der Spieler sich bewegt auf diesen Wert */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**@param directionUD  Setzt die vertikale Richtung des Spielers auf diesen Wert*/
    public void setDirectionUD(int directionUD) {
        this.directionUD = directionUD;
    }


    /**@return Gibt die Anzahl an verfügbaren Sprüngen zurück*/
    public int getJumpsAvailable() {
        return jumpsAvailable;
    }

    /**@param jumpsAvailable  Setzt die Anzahl an Sprüngen auf diesen Wert*/
    public void setJumpsAvailable(int jumpsAvailable) {
        this.jumpsAvailable = jumpsAvailable;
    }

    /**@return Gibt den Cooldown für einen nächsten Sprung zurück*/
    public double getJumpCooldown() {
        return jumpCooldown;
    }

    /**@param jumpCooldown  Setzt den Cooldown für Sprünge auf diesen Wert*/
    public void setJumpCooldown(double jumpCooldown) {
        this.jumpCooldown = jumpCooldown;
    }


    /**@return Gibt den Cooldown nach einer Attacke zurück*/
    public double getAttackWindDown() {
        return attackWindDown;
    }

    /**@return Gibt die horizontale Richtung des Spielers zurück*/
    public int getDirectionLR() {
        return directionLR;
    }

    /**@return Gibt die vertikale Richtung des Spielers zurück*/
    public int getDirectionUD() {
        return directionUD;
    }

    /**@return Gibt die Anzahl der Leben des Spielers zurück*/
    public int getStocks() {
        return stocks;
    }

    /**@param stocks  Setzt die Anzahl der Leben des Spielers auf diesen Wert*/
    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    /**Entfernt den Renderer des Spielers*/
    public void removeRenderer(){
        Game.getEnvironment().removeRenderable(pr);
    }
}
