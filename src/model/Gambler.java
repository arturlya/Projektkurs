package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;

import static control.Timer.dt;

public class Gambler extends Player {

    private double hitPercentage;
    private double result;
    private int damage;
    private double buffTimer;
    private boolean buffed;

    private boolean teleportToPlayer;


    private double slotCooldown;
    private int[] slotMachine;

    public Gambler(double x, double y,boolean playable){
        super(x,y,playable);
        setWidth(96);
        setHeight(96);
        setName("Gambler");
        resetKombo();
        slotCooldown = 0;
        slotMachine = new int[3];
        buffed = false;
        result = Math.random();
        teleportToPlayer = false;
    }


    @Override
    public void update() {
        super.update();
        if(slotMachineFilled()){
            evaluateSlotMachine();
            clearSlotMachine();
        }if(buffed){
            if(buffTimer >= 0) {
                buffTimer = buffTimer - 1 * dt;
            } else {
                hitPercentage = 0.8;
                knockbackPercentage = knockbackPercentage - 10;
                speed = speed -100;
                System.out.println("Boosts removed");
                buffed = false;
            }
        }
    }

    @Override
    public void normalAttackRun() {
        if(result<=hitPercentage){
            System.out.println("Attack succeeded");
            setSlotCooldown(0);
            if(lookingAt == 1) {
                hurtbox.setRelativeRect(hitbox.width,hitbox.height*0.8,hitbox.width*0.4,hitbox.height*0.2);
            }else{
                hurtbox.setRelativeRect(-hitbox.width*0.4,hitbox.height*0.8,hitbox.width*0.4,hitbox.height*0.2);
            }

            hurtbox.setDamage(damage);
            hurtbox.setKnockback(damage/2);

            attackWindUp = 0.1;
            attackHurtTime = 0.4;
            attackWindDown = 0.2;

            hitPercentage = hitPercentage-0.2;
            damage = damage +2;
            System.out.println("Current Damage : "+damage);
        }else{
            setSlotCooldown(0);
            resetKombo();
            attackWindDown = 1;
            System.out.println("Attack not succeeded");
            addSlotValue(-1);
        }
        result = Math.random();
    }

    @Override
    public void normalAttackDown() {
        if(result<=hitPercentage){
            System.out.println("Attack succeeded");
            setSlotCooldown(0);
            hurtbox.setRelativeRect(-hitbox.width*0.7,hitbox.height*0.2,hitbox.width+hitbox.width*1.4,hitbox.height*0.8);

            hurtbox.setDamage(damage);
            hurtbox.setKnockback(damage/2);
            attackWindUp = 0.1;
            attackHurtTime = 0.3;
            attackWindDown = 0.3;
            hitPercentage = hitPercentage-0.2;
            damage = damage +2;
            System.out.println("Current Damage : "+damage);

        }else{
            resetKombo();
            attackWindDown = 1;
            setSlotCooldown(0);
            System.out.println("Attack not succeeded");
            addSlotValue(-1);
        }
        result = Math.random();
    }

    @Override
    public void normalAttackUp() {
        if(result <= hitPercentage){
            System.out.println("Attack succeeded");
            setSlotCooldown(0);
            hurtbox.setRelativeRect(0,-hitbox.height*0.4,hitbox.width,hitbox.height*0.4);
            hurtbox.setDamage(damage);
            hurtbox.setKnockback(damage/2);
            attackWindUp = 0.1;
            attackHurtTime = 0.2;
            attackWindDown = 0.4;

            hitPercentage = hitPercentage-0.2;
            damage = damage +2;
            System.out.println("Current Damage : "+damage);
        }else{
            System.out.println("Attack not succeeded");
            attackWindDown = 1;
            setSlotCooldown(0);
            resetKombo();
            addSlotValue(-1);
        }
        result = Math.random();
    }

    @Override
    public void normalAttackStand() {
        if(result<=hitPercentage){
            System.out.println("Attack succeeded");
            setSlotCooldown(0);
            if(lookingAt == 1) {
                hurtbox.setRelativeRect(hitbox.width , hitbox.height * 0.4, hitbox.width *0.4, hitbox.height * 0.6);
            }else{
                hurtbox.setRelativeRect(-hitbox.width*0.4 , hitbox.height * 0.4, hitbox.width *0.4, hitbox.height * 0.6);
            }
            hurtbox.setDamage(damage);
            hurtbox.setKnockback(damage/2);
            attackWindUp = 0.015;
            attackHurtTime = 0.2;
            attackWindDown = 0.015;

            hitPercentage = hitPercentage-0.2;
            damage = damage +2;
            System.out.println("Current Damage : "+damage);
        }else{
            resetKombo();
            attackWindDown = 1;
            System.out.println("Attack not succeeded");
            setSlotCooldown(0);
            addSlotValue(-1);
        }
        result = Math.random();
    }

    @Override
    public void specialAttackRun() {
        attackWindUp = 0.3;
        throwCoins();
        attackWindDown = 0.4;
    }

    @Override
    public void specialAttackDown() {
        attackWindUp = 0.5;
        shieldActive = true;
        attackWindDown = 1;
    }

    @Override
    public void specialAttackUp() {
        if(result*1.5 <= hitPercentage){
            teleportToPlayer = true;
            hitPercentage = hitPercentage -0.1;
            attackWindDown = 2;
        }else{
            resetKombo();
            System.out.println("Not succeeded");
            attackWindDown = 3;
        }
    }




    @Override
    public void specialAttackStand() {
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,5, new Vector2D(1700,0),damage,1);
        attackWindDown = 0.3;
    }

    private void resetKombo(){
        hitPercentage = 0.9;
        damage = 4;
    }

    private void throwCoins(){
        for(int i=0;i<damage;i++) {
            Projectile coin;
            if (lookingAt == 0){
                coin = new Projectile(this, getX(), getY() + hitbox.height * 0.25, 5, 5, new Vector2D(Math.random() * 100 + 150, -(Math.random() * 100 + 400)), 3, 1);
            }else{
                coin = new Projectile(this, getX()+hitbox.width, getY() + hitbox.height * 0.25, 5, 5, new Vector2D(Math.random() * 100 + 150, -(Math.random() * 100 + 400)), 3, 1);
            }
            Game.getEnvironment().add(coin);
            Game.getEnvironment().add(coin, RenderType.NORMAL);
        }
    }


    /**
     * Methode mit der Werte in der Slotmachine gespeichert werden.
     *
     * @param sign Wert für die Slotmachine -> Darf nur 1 für 'gut' und -1 für 'schlecht' beinhalten!
     */
    public void addSlotValue(int sign){
        if(slotCooldown <= 0 && !buffed) {
            if (sign == 1 || sign == -1)
                for (int i = 0; i < slotMachine.length; i++) {
                    if (slotMachine[i] == 0) {
                        slotMachine[i] = sign;
                        System.out.print(slotMachine[i] + "|");
                        break;
                    } else {
                        System.out.print(slotMachine[i] + "|");
                    }
                }
            System.out.println("");
        }
        slotCooldown = 1;
    }

    private void clearSlotMachine(){
        for(int i=0;i<slotMachine.length;i++){
            slotMachine[i] = 0;
        }
    }

    private boolean slotMachineFilled(){
        boolean tmp = true;
        for(int i=0;i<slotMachine.length;i++){
            if(slotMachine[i] == 0)
                tmp = false;
        }
        return tmp;
    }


    public double getSlotCooldown(){return slotCooldown;}
    public void setSlotCooldown(double value){slotCooldown = value;}
    public double getBuffTimer(){return buffTimer;}

    private int countSlotMachine(int number){
        int counter = 0;
        for(int i=0;i<slotMachine.length;i++){
            if(slotMachine[i] == number)
                counter++;
        }
        return counter;
    }

    private void evaluateSlotMachine(){
        switch (countSlotMachine(1)){
            case 0:
                attackWindDown = 10;
                break;
            case 1:
                knockbackPercentage = knockbackPercentage + 10;
                break;
            case 2:
                speed = speed + 100;
                break;
            case 3:
                hitPercentage = hitPercentage + 0.7;
                break;
        }
        System.out.println("added buffs");
        buffed = true;
        buffTimer = 30;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public boolean isTeleportedToPlayer() {
        return teleportToPlayer;
    }

    public void setTeleportToPlayer(boolean teleportToPlayer) {
        this.teleportToPlayer = teleportToPlayer;
    }
}
