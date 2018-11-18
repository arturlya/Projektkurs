package model;

public class Mage extends Player{

    private boolean teleUsed;

    public Mage(boolean playable){
        super(playable);
        setWidth(50);
        setHeight(100);
        setName("Mage");
    }

    @Override
    public void update() {
        super.update();
        if(!inAir){
            teleUsed = false;
        }
    }

    @Override
    public void normalAttackRun() {
        //hurtbox.setRect(this.getX()-hitbox.width+(hitbox.width+50)*directionLR,this.getY()+hitbox.height/2-25,50,50);
        if(lookingAt == 1){
            hurtbox.setRelativeRect(hitbox.width,hitbox.height/4,hitbox.width,hitbox.height*0.5);
        }else{
            hurtbox.setRelativeRect(-hitbox.width,hitbox.height/4,hitbox.width,hitbox.height*0.5);
        }
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.1;
        attackHurtTime = 0.3;
        attackWindDown  =0.6;
    }

    @Override
    public void normalAttackDown() {
        //hurtbox.setRect(this.getX()-50,this.getY()+70,150,30);
        hurtbox.setRelativeRect(-hitbox.width,hitbox.height*0.7,hitbox.width*3,hitbox.height*0.3);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.1;
        attackHurtTime = 0.6;
        attackWindDown  =0.7;
    }

    @Override
    public void normalAttackUp() {
        //hurtbox.setRect(this.getX()-10,this.getY()-100,70,100);
        hurtbox.setRelativeRect(-hitbox.width*0.2,-hitbox.height,hitbox.width*1.4,hitbox.height);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.5;
        attackHurtTime = 0.4;
        attackWindDown  =0.2;
    }

    @Override
    public void normalAttackStand() {
        //hurtbox.setRect(this.getX()-70+(70+hitbox.width)*lookingAt,this.getY()+25,70,50);
        if(lookingAt == 1) {
            hurtbox.setRelativeRect(hitbox.width,hitbox.height*0.25,hitbox.width*1.4,hitbox.height*0.5);
        }else{
            hurtbox.setRelativeRect(-hitbox.width*1.4,hitbox.height*0.25,hitbox.width*1.4,hitbox.height*0.5);
        }
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.5;
        attackHurtTime = 0.2;
        attackWindDown  =0.3;
    }

    @Override
    public void specialAttackRun() {
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,10);
        attackWindDown = 0.3;
    }

    @Override
    public void specialAttackDown() {
        attackWindUp = 0.5;
        shieldActive = true;
        attackWindDown = 1;
    }

    @Override
    public void specialAttackUp() {
        if(!teleUsed) {
            this.setY(this.getY() - hitbox.height * 2);
            inAir = true;
            this.verticalSpeed = 0;
            teleUsed = true;
        }
    }

    @Override
    public void specialAttackStand() {
        //hurtbox.setRect(this.getX()-75,this.getY()-50,200,150);
        hurtbox.setRelativeRect(-hitbox.width*1.5,-hitbox.height*0.5,hitbox.width*4,hitbox.height*1.5);
        hurtbox.setDamage(0);
        hurtbox.setKnockback(0);
        attackWindUp = 0.5;
        attackHurtTime = 0.3;
        attackWindDown  =0.4;
    }
}
