package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.util.geom.Vector2D;

import static control.Timer.dt;

/**
 * Klasse Gambler.
 * Erbt von Player.
 * Arbeitet mit Wahrscheinlichkeiten in Form von Matrizenrechnung.
 * Damit sich der Zufall lohnt, steigt der Schaden mit der Trefferserie.
 *
 */
public class Gambler extends Player {

    /** Speichert die Zufallszahl, die bei der nächsten Attacke erreicht werden muss*/
    private double result;
    /** Speichert den Schaden den der Gambler momentan austeilt*/
    private int damage;
    /** Timer für die Buffs der Slotmachine*/
    private double buffTimer;
    /** Merkt sich, ob momentan Buffs auf dem Gambler wirken*/
    private boolean buffed;

    /** Speichert, ob der Gambler sich zu einem anderen Spieler teleportieren soll*/
    private boolean teleportToPlayer;

    /** Cooldown für das Hinzufügen von Werten in der Slotmachine*/
    private double slotCooldown;
    /** Slotmachine, die zwischen 1(Treffer) und -1(Pleite) unterscheidet, sobald 3 Werte vorhanden sind wird sie ausgewertet*/
    private int[] slotMachine;
    /** Übergangsmatrix des stochastischen Prozesses*/
    private double[][] uMatrix;
    /** Die Verteilung der Wahrscheinlichkeiten, wird nach Attacke mit der Übergangsmatrix multipliziert*/
    private double[][] verteilung;

    /**
     * Konstruktor der Klasse Gambler.
     *
     * @param x X-Position des Gamblers
     * @param y Y-Position des Gamblers
     * @param isPlayerTester gibt an, ob der Spieler im PlayerTester oder im GameClient erstellt wurde
     */
    public Gambler(double x, double y, boolean isPlayerTester){
        super(x,y,isPlayerTester);
        uMatrix = new double[5][5];
        verteilung = new double[1][5];
        verteilung[0][0] = 1;
        for(int i =0;i<uMatrix.length;i++){
            if(i==0){
                uMatrix[i][0] = 0.1;
                uMatrix[i][1] = 0.9;
            }else if(i==1){
                uMatrix[i][0] = 0.3;
                uMatrix[i][2] = 0.7;
            }else if(i==2){
                uMatrix[i][0] = 0.5;
                uMatrix[i][3] = 0.5;
            }else if(i == 3){
                uMatrix[i][0] = 0.7;
                uMatrix[i][2] = 0.3;
            }else if(i == 4){
                uMatrix[i][0] = 1;
            }
        }
        matrixMultiplikation(verteilung,uMatrix);
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

    /**
     * Update des Interfaces IUpdateable
     */
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
                if (knockbackPercentage >= 10) knockbackPercentage = knockbackPercentage - 10;
                else knockbackPercentage = 0;
                speed = speed -100;
                System.out.println("Boosts removed");
                buffed = false;
            }
        }
    }

    /**
     * Implementation der normalAttackRun vom Player.
     * Arbeitet mit Wahrscheinlichkeiten.
     */
    @Override
    public void normalAttackRun() {
        System.out.println(getMatrix()[0]);
        if(result>=getMatrix()[0]){
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

            //hitPercentage = hitPercentage-0.2;
            verteilung = matrixMultiplikation(verteilung,uMatrix);
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
    /**
     * Implementation der normalAttackDown vom Player.
     * Arbeitet mit Wahrscheinlichkeiten.
     */
    @Override
    public void normalAttackDown() {
        if(result>=getMatrix()[0]){
            System.out.println("Attack succeeded");
            setSlotCooldown(0);
            hurtbox.setRelativeRect(-hitbox.width*0.7,hitbox.height*0.2,hitbox.width+hitbox.width*1.4,hitbox.height*0.8);

            hurtbox.setDamage(damage);
            hurtbox.setKnockback(damage/2);
            attackWindUp = 0.1;
            attackHurtTime = 0.3;
            attackWindDown = 0.3;
            verteilung = matrixMultiplikation(verteilung,uMatrix);
            //hitPercentage = hitPercentage-0.2;
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

    /**
     * Implementation der normalAttackUp vom Player.
     * Arbeitet mit Wahrscheinlichkeiten.
     */
    @Override
    public void normalAttackUp() {
        if(result>=getMatrix()[0]){
            System.out.println("Attack succeeded");
            setSlotCooldown(0);
            hurtbox.setRelativeRect(0,-hitbox.height*0.4,hitbox.width,hitbox.height*0.4);
            hurtbox.setDamage(damage);
            hurtbox.setKnockback(damage/2);
            attackWindUp = 0.1;
            attackHurtTime = 0.2;
            attackWindDown = 0.4;
            verteilung = matrixMultiplikation(verteilung,uMatrix);
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
    /**
     * Implementation der normalAttackStand vom Player.
     * Arbeitet mit Wahrscheinlichkeiten.
     */
    @Override
    public void normalAttackStand() {
        if(result>=getMatrix()[0]){
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
            verteilung = matrixMultiplikation(verteilung,uMatrix);
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
    /**
     * Implementation der specialAttackRun vom Player.
     * Wirft Münzen auf Gegner.
     * Arbeitet nicht mit Wahrscheinlichkeiten.
     */
    @Override
    public void specialAttackRun() {
        attackWindUp = 0.3;
        throwCoins();
        attackWindDown = 0.4;
    }
    /**
     * Implementation der specialAttackDown vom Player.
     * Verteidigt Angriffe.
     * Arbeitet nicht mit Wahrscheinlichkeiten.
     */
    @Override
    public void specialAttackDown() {
        attackWindUp = 0.5;
        shieldActive = true;
        attackWindDown = 1;
    }
    /**
     * Implementation der specialAttackUp vom Player.
     * Teleportiert sich zu einem zufälligen Spieler.
     * Arbeitet mit Wahrscheinlichkeiten.
     */
    @Override
    public void specialAttackUp() {
        if(result*1.5>=getMatrix()[0]){
            teleportToPlayer = true;
            verteilung = matrixMultiplikation(verteilung,uMatrix);
            attackWindDown = 2;
        }else{
            resetKombo();
            System.out.println("Not succeeded");
            attackWindDown = 3;
        }
    }



    /**
     * Implementation der specialAttackStand vom Player.
     * Schießt Projektil.
     * Arbeitet nicht mit Wahrscheinlichkeiten.
     */
    @Override
    public void specialAttackStand() {
        attackWindUp = 0.2;
        shoot(hitbox.x,hitbox.y+hitbox.height*0.25,20,5, new Vector2D(1700,0),damage,1);
        attackWindDown = 0.3;
    }

    /**
     * Setzt die Verteilung und den Schaden zurück.
     */
    private void resetKombo(){
        for(int i=0;i<verteilung.length;i++){
            for(int j=0;j<verteilung[i].length;j++) {
                verteilung[i][j] = 0;
            }
        }
        verteilung[0][0] = 1;
        verteilung = matrixMultiplikation(verteilung,uMatrix);
        damage = 4;
    }

    /**
     * Erstellt so viele Projektile, wie der momentane Schaden ist.
     */
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

    /**
     * Setzt die Slotmachine zurück.
     */
    private void clearSlotMachine(){
        for(int i=0;i<slotMachine.length;i++){
            slotMachine[i] = 0;
        }
    }

    /**
     * @return Gibt zurück, ob die Slotmachine voll ist und ausgewertet werden muss.
     */
    private boolean slotMachineFilled(){
        boolean tmp = true;
        for(int i=0;i<slotMachine.length;i++){
            if(slotMachine[i] == 0)
                tmp = false;
        }
        return tmp;
    }

    /**@return Gibt den Cooldown für die Slotmachine zurück*/
    public double getSlotCooldown(){return slotCooldown;}

    /**@param value Setzt den slotCooldown auf diesen Wert.*/
    public void setSlotCooldown(double value){slotCooldown = value;}
    /**@return Gibt den Timer für die Buffs zurück*/
    public double getBuffTimer(){return buffTimer;}

    /**
     * Zählt die Anzahl des übergebenen Wertes in der Slotmachine
     *
     * @param number Wert, der gezählt werden soll.
     * @return Anzahl des Wertes in der Slotmachine.
     */
    private int countSlotMachine(int number){
        int counter = 0;
        for(int i=0;i<slotMachine.length;i++){
            if(slotMachine[i] == number)
                counter++;
        }
        return counter;
    }

    /**
     * Wertet die Slotmachine aus und gibt Buffs.
     */
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
                for(int i=0;i<verteilung.length;i++){
                    for(int j=0;j<verteilung[i].length;j++) {
                        verteilung[i][j] = 0;
                    }
                }
                verteilung[0][0] = 1;
                verteilung = matrixMultiplikation(verteilung,uMatrix);
                break;
        }
        System.out.println("added buffs");
        buffed = true;
        buffTimer = 30;
    }

    /**
     * Multipliziert zwei zweidimensionale Arrays, als Matrizen, miteinander.
     *
     * @param v Erste Matrix.
     * @param u Zweite Matrix.
     * @return Gibt eine Ergebnismatrix zurück.Wenn die Matrizen nicht multiplizierbar sind, gibt es null zurück.
     */
    public static double[][] matrixMultiplikation(double[][] v,double[][] u){
        int vX = v.length;
        int vY = v[0].length;
        int uX = u.length;
        int uY = u[0].length;
        if(vY == uY && vX == uX){
            double[][] tmp = new double[vX][uY];
            for(int i=0;i<vX;i++){
                for(int j=0;j<uY;j++){
                    for(int k=0;k<vY;k++) {
                        tmp[i][j] += v[i][k] * u[k][j];
                    }
                }

            }
            return tmp;
        }
        /*else if(vX == uX && vY == uY) {
            double[][] tmp = new double[uX][vY];

            for(int i=0;i<uX;i++){
                for(int j=0;j<vY;j++){
                    for(int k=0;k<uY;k++) {
                        tmp[i][j] += v[i][k] * u[k][j];
                    }
                }

            }
            return tmp;
        }*/else {
            System.err.println("Dimensionsfehler");
            return null;
        }
    }
    /**@return Gibt die benötigte Zufallszahl für dn nächsten Angriff zurück*/
    public double getResult() {
        return result;
    }
    /**@param result Setzt die Zufallszahl auf diesen Wert*/
    public void setResult(double result) {
        this.result = result;
    }

    /**@return Gibt zurück, ob der Spieler sich zu einem anderen Spieler teleportiert.*/
    public boolean isTeleportedToPlayer() {
        return teleportToPlayer;
    }
    /**@param teleportToPlayer Setzt den Teleport auf diesen Wert*/
    public void setTeleportToPlayer(boolean teleportToPlayer) {
        this.teleportToPlayer = teleportToPlayer;
    }

    /** Gibt die momentane Verteilung als 5x1 Matrix wieder*/
    public double[] getMatrix(){
        return verteilung[0];
    }
}
