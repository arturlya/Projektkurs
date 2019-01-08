package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;

public class Gambler extends Player {

    private double hitPercentage;
    private double result;
    private int damage;

    private int[] slotMachine;

    public Gambler(double x, double y,boolean playable){
        super(x,y,playable);
        setWidth(96);
        setHeight(96);
        setName("Gambler");
        resetKombo();
        slotMachine = new int[3];
    }

    @Override
    public void update() {
        super.update();

    }

    @Override
    public void normalAttackRun() {

    }

    @Override
    public void normalAttackDown() {
        result = Math.random();
        if(result<hitPercentage){
            System.out.println("Attack succeeded");

            hurtbox.setRelativeRect(-hitbox.width*0.7,hitbox.height*0.2,hitbox.width+hitbox.width*1.4,hitbox.height*0.8);

            hurtbox.setDamage(damage);
            hurtbox.setKnockback(3/damage);
            attackWindUp = 0.1;
            attackHurtTime = 0.3;
            attackWindDown = 0.2;
            hitPercentage = hitPercentage-0.1;
            damage++;
            System.out.println("Current Damage : "+damage);

        }else{
            resetKombo();
            attackWindDown = 1;
            System.out.println("Attack not succeeded");

        }
    }

    @Override
    public void normalAttackUp() {

    }

    @Override
    public void normalAttackStand() {
        result = Math.random();
        if(result<=hitPercentage){
            System.out.println("Attack succeeded");

            if(lookingAt == 1) {
                hurtbox.setRelativeRect(hitbox.width , hitbox.height * 0.4, hitbox.width *0.4, hitbox.height * 0.6);
            }else{
                hurtbox.setRelativeRect(-hitbox.width*0.4 , hitbox.height * 0.4, hitbox.width *0.4, hitbox.height * 0.6);
            }
            hurtbox.setDamage(damage);
            hurtbox.setKnockback(3/damage);
            attackWindUp = 0.015;
            attackHurtTime = 0.2;
            attackWindDown = 0.015;
            hitPercentage = hitPercentage-0.02;
            damage++;
            System.out.println("Current Damage : "+damage);
        }else{
            resetKombo();
            attackWindDown = 1;
            System.out.println("Attack not succeeded");
        }
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

    }

    @Override
    public void specialAttackStand() {
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,10, new Vector2D(1700,0),damage,1);
        attackWindDown = 0.3;
    }

    private void resetKombo(){
        hitPercentage = 0.8;
        damage = 2;
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
        if(sign == 1 || sign == -1)
        for(int i=0;i<slotMachine.length;i++){
            if(slotMachine[i] == 0){
                slotMachine[i] = sign;
                break;
            }
        }
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
}
