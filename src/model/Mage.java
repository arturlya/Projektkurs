package model;

public class Mage extends Player{

    public Mage(boolean playable){
        super(playable);
        setX(300);
        setY(300);
        setWidth(50);
        setHeight(100);
        setName("Mage");
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void normalAttackRun() {
        hurtbox.setRect(this.getX()-hitbox.width+(hitbox.width+50)*directionLR,this.getY()+hitbox.height/2-25,50,50);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.1;
        attackHurtTime = 0.3;
        attackWindDown  =0.6;
    }

    @Override
    public void normalAttackDown() {
        hurtbox.setRect(this.getX()-50,this.getY()+70,150,30);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.1;
        attackHurtTime = 0.6;
        attackWindDown  =0.7;
    }

    @Override
    public void normalAttackUp() {
        hurtbox.setRect(this.getX()-10,this.getY()-100,70,100);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.5;
        attackHurtTime = 0.4;
        attackWindDown  =0.2;
    }

    @Override
    public void normalAttackStand() {
        hurtbox.setRect(this.getX()-70+(70+hitbox.width)*lookingAt,this.getY()+25,70,50);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.5;
        attackHurtTime = 0.2;
        attackWindDown  =0.3;
    }

    @Override
    public void specialAttackRun() {

    }

    @Override
    public void specialAttackDown() {

    }

    @Override
    public void specialAttackUp() {
        this.setY(this.getY()-200);
        attackWindDown = 0.3;
    }

    @Override
    public void specialAttackStand() {
        hurtbox.setRect(this.getX()-75,this.getY()-50,200,150);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.5;
        attackHurtTime = 0.3;
        attackWindDown  =0.4;
    }
}
