package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.input.Input;
import model.Screens.IngameScreen;
import model.abitur.datenstrukturen.List;
import model.abitur.netz.Client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameClient extends Client implements IUpdateable {

    private Player player;
    private boolean ready;
    private boolean gameStarted;
    private int playerNumber;

    private List<Player> others;

    /**
     * Konstruktor der Klasse GameClient.
     * Besitzt einen KeyListener zum voten.
     *
     * @param ip IP des Servers
     * @param port Port des Servers
     */
    public GameClient(String ip, int port){
        super(ip,port);
        ready = false;
        others = new List<>();
        choosePlayer(1);
        player.setY(10);
        player.setX(600);
        Game.getEnvironment().add(player);
        Game.getEnvironment().add(player,RenderType.NORMAL);
        //init();
        Input.getLoop().attach(this);
        Input.keyboard().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER && !gameStarted){
                    if (ready) {
                        send("STARTfalse");
                        ready = false;
                    }else {
                        choosePlayer(1);
                        send("STARTtrue");
                        ready = true;
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    /**
     * Update des Interfaces IUpdateable
     */
    @Override
    public void update(){
        if(player != null)
        if(player.getY()>2000){
            player.setY(50);
        }
        if (gameStarted && player.isMoving()) {
           // send("POSITION"+playerNumber+"#" + player.getHorizontalSpeed() + "#" + player.getVerticalSpeed());
            send("POSITION"+playerNumber+"#" + player.getX() + "#" + player.getY());
        }
        /*
        if(gameStarted && player.getHurtbox().isHurting()){
            //Also ... 0=player,1=x,2=y,3=width,4=height,5=isHurting,6=knockback,7=damage,8=relativeX,9=relativeY ...ufff...
            send("HURT"+player.getPlayerNumber()+"#"+player.getHurtbox().getX()+"#"+player.getHurtbox().getY()+"#"+player.getHurtbox().width+"#"+player.getHurtbox().height+"#"+player.getHurtbox().isHurting()+"#"+player.getHurtbox().getKnockback()+"#"+player.getHurtbox().getDamage()+"#"+player.getHurtbox().getRelativeX()+"#"+player.getHurtbox().getRelativeY());
        }
        if(gameStarted ){
            send("SHIELD"+player.getPlayerNumber()+"#"+player.isShieldActive());
        }
        if(gameStarted && player.getProjectile() != null){
            //Das selbe wie bei Hurtbox nur mit dem Projectile
            send("PROJECTILE"+player.getPlayerNumber()+"#"+player.getProjectile().getHurtbox().getX()+"#"+player.getProjectile().getHurtbox().getY()+"#"+player.getProjectile().getHurtbox().width+"#"+player.getProjectile().getHurtbox().height);//+"#"+player.getProjectile().getHurtbox().isHurting()+"#"+player.getProjectile().getHurtbox().getKnockback()+"#"+player.getProjectile().getHurtbox().getDamage()+"#"+player.getProjectile().getHurtbox().getRelativeX()+"#"+player.getProjectile().getHurtbox().getRelativeY());
        }*/

        //System.out.println(player.getActiveAttack());
        if(gameStarted && !player.getActiveAttack().equals("")){
            System.out.println("YES!"+player.getActiveAttack());
            send("ATTACK"+playerNumber+"#"+player.getActiveAttack());
        }
        if (playerNumber != 0 && player != null) {
            player.setPlayerNumber(playerNumber);

        }

    }


    /**
     * Verarbeitet die empfangene Nachricht vom Server.
     *
     * @param pMessage Die empfangene Nachricht, wenn
     *                 -pMessage mit "START" dann beginnt das Spiel
     *                 -pMessage mit "PLAYER" dann wird eine Spielernummer vom Server zugewiesen
     *                 //-pMessage mit "CHOOSE"
     *                 -pMessage mit "ALL" bekommt der Client alle Spieler vom Server
     *                 -pMessage mit "POSITION" bekommt der Client die Position eines bestimmten Clients und aktualisiert sie bei sich
     */
    @Override
    public void processMessage(String pMessage) {
        int max = 0;
        if(max == 0) {
            if (pMessage.contains("START")) {
                String[] temp = pMessage.split("START", 2);
                if (temp[1].contains("true")) {
                    gameStarted = true;
                } else {
                    gameStarted = false;
                }
            } else if (pMessage.contains("PLAYER")) {
                String[] temp = pMessage.split("PLAYER");
                playerNumber = Integer.parseInt(temp[1]);
                choosePlayer(1);
            } else if(pMessage.contains("CHOOSE")){
                //choosePlayer(1);
            } else if (pMessage.contains("ALL")) {
                String[] temp = pMessage.split("ALL");
                temp = temp[1].split("NEXT");
                if(getNumberOfOtherPlayers() < Integer.parseInt(temp[0])) {
                    for (int i = 1; i < temp.length; i++) {
                        //Aufpassen temp0
                        String[] charInfo = temp[i].split("#", 2);
                        if(Integer.parseInt(charInfo[0]) != playerNumber){
                        // if (Integer.parseInt(charInfo[0]) != player.getPlayerNumber()) {
                            boolean alreadyKnown = false;
                            others.toFirst();
                            while(others.hasAccess()){
                                if(others.getContent().getPlayerNumber() == Integer.parseInt(charInfo[0])){
                                    alreadyKnown = true;
                                }
                                others.next();
                            }
                            if(!alreadyKnown) {
                                if (charInfo[1].equals("Warrior")) {
                                    Player otherPlayer = new Warrior(400,50,false);
                                    otherPlayer.setPlayerNumber(Integer.parseInt(charInfo[0]));
                                    others.append(otherPlayer);
                                    Game.getEnvironment().add(otherPlayer);
                                    Game.getEnvironment().add(otherPlayer, RenderType.NORMAL);
                                    //ingameScreen.addGravObject(otherPlayer);
                                    System.out.println("Added player");
                                } else if (charInfo[1].equals("Mage")) {
                                    Player otherPlayer = new Mage(400,50,false);
                                    otherPlayer.setPlayerNumber(Integer.parseInt(charInfo[0]));
                                    others.append(otherPlayer);
                                    Game.getEnvironment().add(otherPlayer);
                                    Game.getEnvironment().add(otherPlayer, RenderType.NORMAL);
                                    //ingameScreen.addGravObject(otherPlayer);
                                    System.out.println("Added player");
                                }
                            }

                        }
                    }
                }
            } else if (pMessage.contains("POSITION")) {
                String[] temp = pMessage.split("POSITION");
                temp = temp[1].split("#");
                if (Integer.parseInt(temp[0]) != player.getPlayerNumber()) {

                    int posInList = Integer.parseInt(temp[0]) - 2;
                    others.toFirst();
                    while (posInList > 0) {
                        others.next();
                        posInList--;
                    }
                    if (others.hasAccess()) {
                        others.getContent().setX(Double.parseDouble(temp[1]));
                        others.getContent().setY(Double.parseDouble(temp[2]));
                     //   others.getContent().setHorizontalSpeed(Double.parseDouble(temp[1]));
                     //   others.getContent().setVerticalSpeed(Double.parseDouble(temp[2]));
                    }
                }
            }/*else if(pMessage.contains("HURT")){
                String[] temp = pMessage.split("HURT");
                temp = temp[1].split("#");
                if(Integer.parseInt(temp[0])!= player.getPlayerNumber()){
                    int posInList = Integer.parseInt(temp[0]) - 2;
                    others.toFirst();
                    while (posInList > 0) {
                        others.next();
                        posInList--;
                    }
                    if (others.hasAccess()) {
                        others.getContent().setHurtbox(new Hurtbox(Double.parseDouble(temp[1]),Double.parseDouble(temp[2]),(int)Double.parseDouble(temp[3]),(int)Double.parseDouble(temp[4])));
                        if(temp[5].equals("true")){
                            others.getContent().getHurtbox().setHurting(true);
                        }else{
                            others.getContent().getHurtbox().setHurting(false);
                        }
                        others.getContent().getHurtbox().setKnockback((int)Double.parseDouble(temp[6]));
                        others.getContent().getHurtbox().setDamage((int)Double.parseDouble(temp[7]));
                        others.getContent().getHurtbox().setRelativeX(Double.parseDouble(temp[8]));
                        others.getContent().getHurtbox().setRelativeY(Double.parseDouble(temp[9]));
                    }
                }
            }else if(pMessage.contains("SHIELD")){
                String[] temp = pMessage.split("SHIELD");
                temp = temp[1].split("#");
                if(Integer.parseInt(temp[0])!= player.getPlayerNumber()) {
                    int posInList = Integer.parseInt(temp[0]) - 2;
                    others.toFirst();
                    while (posInList > 0) {
                        others.next();
                        posInList--;
                    }
                    if (others.hasAccess()) {
                        others.getContent().setShieldActive(true);
                    }
                }
            }else if(pMessage.contains("PROJECTILE")){
                String[] temp = pMessage.split("PROJECTILE");
                temp = temp[1].split("#");
                if(Integer.parseInt(temp[0])!= player.getPlayerNumber()) {
                    int posInList = Integer.parseInt(temp[0]) - 2;
                    others.toFirst();
                    while (posInList > 0) {
                        others.next();
                        posInList--;
                    }
                    if (others.hasAccess()) {
                        if (others.getContent().getProjectile() == null) {
                            others.getContent().shoot(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), (int) Double.parseDouble(temp[2]), (int) Double.parseDouble(temp[3]));
                        }

                    }
                }
            }*/
            else if(pMessage.contains("ATTACK")){
                System.out.println(pMessage);
                String[] temp = pMessage.split("ATTACK");
                temp = temp[1].split("#");
                if (Integer.parseInt(temp[0]) != player.getPlayerNumber()) {
                    int posInList = Integer.parseInt(temp[0]) - 2;
                    others.toFirst();
                    while (posInList > 0) {
                        others.next();
                        posInList--;
                    }
                    if (others.hasAccess()) {
                        switch (temp[1]){
                            case "nAS":
                                others.getContent().normalAttackStand();
                                break;
                            case "nAR":
                                others.getContent().normalAttackRun();
                                break;
                            case "nAD":
                                others.getContent().normalAttackDown();
                                break;
                            case "nAU":
                                others.getContent().normalAttackUp();
                                break;
                            case "sAS":
                                others.getContent().specialAttackStand();
                                break;
                            case "sAR":
                                others.getContent().specialAttackRun();
                                break;
                            case "sAD":
                                others.getContent().specialAttackDown();
                                break;
                            case "sAU":
                                others.getContent().specialAttackUp();
                                break;
                        }
                    }
                }
            }
            max++;
        }
    }


    /**
     * Methode zum Initialisieren.
     */
    protected void init(){
        List<IEntity> entityList = new List<>();
        entityList.append(player);
        entityList.toFirst();
        while(entityList.hasAccess()){
            Game.getEnvironment().add(entityList.getContent());
            //Game.getEnvironment().add(e);

            if(entityList.getContent() instanceof GravitationalObject){
                Game.getEnvironment().add((GravitationalObject)entityList.getContent(),RenderType.NORMAL);
                //ingameScreen.addGravObject((GravitationalObject)entityList.getContent());
            }
            entityList.next();
        }
    }

    /**
     * @return Gibt die Anzahl an gegnerischen Spielern, die der Client kennt.
     */
    private int getNumberOfOtherPlayers(){
        int value = 0;

        others.toFirst();
        while(others.hasAccess()){
            value++;
            others.next();
        }
        others.toFirst();

        return value;
    }

    /**
     * Sucht einen Spieler aus.
     * Sollte ein Spieler ausgesucht sein, sendet er bei erneutem Aufruf dem Server die Zahl des Spielers
     *
     * @param number Index des gewünschten Spielers
     */
    private void choosePlayer(int number){
        if(player == null) {
            switch (number) {
                case 1:
                    player = new Warrior(400,50,true);
                    break;
                case 2:
                    player = new Mage(400,50,true);


                    break;
            }
        }else{
            switch (number) {
                case 1:
                    send("PLAYER" + player.getPlayerNumber() + "PLAYER" + 1);
                    break;
                case 2:
                    send("PLAYER" + player.getPlayerNumber() + "PLAYER" + 2);
                    break;
            }
        }
    }
}
