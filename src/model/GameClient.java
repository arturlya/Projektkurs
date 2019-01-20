package model;

import control.ScreenController;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.input.Input;
import model.Maps.Map;
import model.Screens.IngameScreen;
import model.Screens.MenuScreen;
import model.abitur.datenstrukturen.List;
import model.abitur.netz.Client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static control.Timer.dt;

public class GameClient extends Client implements IUpdateable {

    private Player player;
    private boolean ready;
    public static boolean gameStarted;
    private int playerNumber;
    private double coolDown;
    private int i;
    private int chosenPlayer;

    private boolean finalChoose,drawnPlayer;
    private String allPlayer = "";

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
        System.out.println("Running GameClient");
        ready = false;
        others = new List<>();
        Input.getLoop().attach(this);
    }

    /**
     * Update des Interfaces IUpdateable
     */
    @Override
    public void update(){

        i = 1;

        if(ScreenController.isIngame() && !drawnPlayer && allPlayer.contains("ALL")){

            String[] temp = allPlayer.split("ALL");
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
                            if (charInfo[1].equals("warrior")) {
                                Player otherPlayer = new Warrior(400,50,false);
                                otherPlayer.setPlayerNumber(Integer.parseInt(charInfo[0]));
                                others.append(otherPlayer);
                                ScreenController.environments.get(1).add(otherPlayer);
                                ScreenController.environments.get(1).add(otherPlayer, RenderType.NORMAL);
                                //ingameScreen.addGravObject(otherPlayer);

                                System.out.println("Added other player");
                            } else if (charInfo[1].equals("Mage")) {
                                Player otherPlayer = new Mage(400,50,false);
                                otherPlayer.setPlayerNumber(Integer.parseInt(charInfo[0]));
                                others.append(otherPlayer);
                                ScreenController.environments.get(1).add(otherPlayer);
                                ScreenController.environments.get(1).add(otherPlayer, RenderType.NORMAL);
                                //ingameScreen.addGravObject(otherPlayer);
                                System.out.println("Added other player");
                            }else if(charInfo[1].equals("Gambler")){
                                Player otherPlayer = new Gambler(400,50,false);
                                otherPlayer.setPlayerNumber(Integer.parseInt(charInfo[0]));
                                others.append(otherPlayer);
                                ScreenController.environments.get(1).add(otherPlayer);
                                ScreenController.environments.get(1).add(otherPlayer, RenderType.NORMAL);
                                System.out.println("Added other player");
                            }
                        }
                    }else{
                        ScreenController.environments.get(1).add(player);
                        ScreenController.environments.get(1).add(player,RenderType.NORMAL);
                        System.out.println("Created own player");
                    }
                }
            }
            drawnPlayer = true;
            System.out.println("Added Players");
        }
        if(gameStarted && coolDown<=0){
            send("POSITIONXY"+playerNumber+"#"+player.getX()+"#"+player.getY()+"#"+player.directionLR+"#"+player.decelerating);
            coolDown = 1;
        }
        if (playerNumber != 0 && player != null) {
            player.setPlayerNumber(playerNumber);
        }
        if(player != null) {

            if (gameStarted ) {
                send("POSITION" + playerNumber + "#" + player.getHorizontalSpeed() + "#" + player.getVerticalSpeed()+"#"+player.directionLR+"#"+player.decelerating);
                // send("POSITION"+playerNumber+"#" + player.getX() + "#" + player.getY());
            }

        }

        if(coolDown>0){
            coolDown = coolDown-1*dt;
        }
        if(gameStarted){
            processInputs();
        }

        if(!gameStarted){

            if(ready != MenuScreen.ready) {
                ready = MenuScreen.ready;
                if (MenuScreen.ready && !finalChoose) {
                    send("STARTtrue");
                    finalChoose = true;
                    System.out.println("You are ready");
                } else if(finalChoose){
                    send("STARTfalse");
                    finalChoose = false;
                    System.out.println("You are not ready");
                }
            }
            chosenPlayer = MenuScreen.playerPick;
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
     *                 -pMessage mit "ATTACK" bekommt der Client eine Attacke, die ein anderer Client ausführt
     */
    @Override
    public void processMessage(String pMessage) {
        int max = 0;
        if(max == 0) {
            if (pMessage.contains("START")) {
                String[] temp = pMessage.split("START", 2);
                if (temp[1].contains("true")) {
                    gameStarted = true;
                    System.out.println("Das Spiel startet!!!");
                } else {
                    gameStarted = false;
                }
            } else if (pMessage.contains("NUMBER")) {
                String[] temp = pMessage.split("NUMBER");
                playerNumber = Integer.parseInt(temp[1]);
                System.out.println("Playernumber : "+playerNumber);


            } else if(pMessage.contains("PLAYER")){
                choosePlayer(chosenPlayer);
            } else if (pMessage.contains("ALL")) {
                allPlayer = pMessage;
            } else if (pMessage.contains("POSITION")) {
                String[] temp = pMessage.split("POSITION");
                if (temp[1].contains("XY")) {
                    temp = temp[1].split("XY");
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
                            others.getContent().setDirectionLR(Integer.parseInt(temp[3]));
                            others.getContent().setDecelerating(Boolean.getBoolean(temp[4]));
                        }
                    }
                }else {
                    temp = temp[1].split("#");
                    if (Integer.parseInt(temp[0]) != player.getPlayerNumber()) {

                        int posInList = Integer.parseInt(temp[0]) - 2;
                        others.toFirst();
                        while (posInList > 0) {
                            others.next();
                            posInList--;
                        }
                        if (others.hasAccess()) {
                            others.getContent().setHorizontalSpeed(Double.parseDouble(temp[1]));
                            others.getContent().setVerticalSpeed(Double.parseDouble(temp[2]));
                            others.getContent().setDirectionLR(Integer.parseInt(temp[3]));
                            others.getContent().setDecelerating(Boolean.getBoolean(temp[4]));
                        }
                    }
                }
            } else if(pMessage.contains("ATTACK")){
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
                                if(others.getContent() instanceof Gambler) {
                                    if(!temp[2].equals("")) {
                                        ((Gambler) others.getContent()).setResult(Double.parseDouble(temp[2]));
                                        others.getContent().normalAttackStand();
                                    }
                                }else{
                                    others.getContent().normalAttackStand();
                                }
                                break;
                            case "nAR":
                                if(others.getContent() instanceof Gambler) {
                                    if(!temp[2].equals("")) {
                                        ((Gambler) others.getContent()).setResult(Double.parseDouble(temp[2]));
                                        others.getContent().normalAttackRun();
                                    }
                                }else{
                                    others.getContent().normalAttackRun();
                                }
                                break;
                            case "nAD":
                                if(others.getContent() instanceof Gambler) {
                                    if(!temp[2].equals("")) {
                                        ((Gambler) others.getContent()).setResult(Double.parseDouble(temp[2]));
                                        others.getContent().normalAttackDown();
                                    }
                                }else{
                                    others.getContent().normalAttackDown();
                                }
                                break;
                            case "nAU":
                                if(others.getContent() instanceof Gambler) {
                                    if(!temp[2].equals("")) {
                                        ((Gambler) others.getContent()).setResult(Double.parseDouble(temp[2]));
                                        others.getContent().normalAttackUp();
                                    }
                                }else{
                                    others.getContent().normalAttackUp();
                                }
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
                                if(others.getContent() instanceof Gambler) {
                                    ((Gambler) others.getContent()).setResult(Double.parseDouble(temp[2]));
                                    others.getContent().specialAttackUp();
                                }else{
                                    others.getContent().specialAttackUp();
                                }
                                break;
                        }
                    }
                }
            }else if(pMessage.contains("JUMP")){
                String[] temp = pMessage.split("JUMP");
                if (Integer.parseInt(temp[1]) != playerNumber) {
                    int posInList = Integer.parseInt(temp[1]) - 2;
                    others.toFirst();
                    while (posInList > 0) {
                        others.next();
                        posInList--;
                    }
                    if (others.hasAccess()) {
                        others.getContent().setVerticalSpeed(-700);
                        others.getContent().setInAir(true);
                        others.getContent().setJumpsAvailable(player.getJumpsAvailable()-1);
                        others.getContent().setJumpCooldown(0.5);
                    }
                }
            }else if(pMessage.contains("LOOKING")){
                String[] temp = pMessage.split("LOOKING");
                temp = temp[1].split("#");
                if (Integer.parseInt(temp[0]) != player.getPlayerNumber()) {
                    int posInList = Integer.parseInt(temp[0]) - 2;
                    others.toFirst();
                    while (posInList > 0) {
                        others.next();
                        posInList--;
                    }
                    if (others.hasAccess()) {
                        others.getContent().setLookingAt(Integer.parseInt(temp[1]));
                    }
                }
            }else if(pMessage.contains("QUIT")){
                String temp = pMessage.split("QUIT")[1];
                int posInList = Integer.parseInt(temp) - 2;
                others.toFirst();
                while (posInList > 0) {
                    others.next();
                    posInList--;
                }
                if(others.hasAccess()) {
                    Game.getEnvironment().removeRenderable(others.getContent());
                    Game.getEnvironment().remove(others.getContent());
                    others.getContent().removeRenderer();
                    System.out.println("Deleted Player " + temp);
                }else {
                    System.out.println("error");
                }
            }
            max++;
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
                    send("PLAYER" + playerNumber + "PLAYER" + 1);
                    break;
                case 2:
                    player = new Mage(400,50,true);
                    send("PLAYER" + playerNumber + "PLAYER" + 2);
                    break;
                case 3:
                    player = new Gambler(400,50,true);
                    send("PLAYER"+playerNumber+"PLAYER"+3);
                    break;
            }
            player.setY(10);
            player.setX(600);


        }
    }
    /**
     * Methode, die, falls der Player spielbar ist, die anderen Input-Mathoden aufruft
     */
    private void processInputs(){
        processInputsDirections();
        processInputsAttacks();
        processInputsJump();
    }

    /**
     * Methode, die die richtungsbeeinflussenden Inputs verarbeitet
     */
    private void processInputsDirections(){
        Input.keyboard().onKeyPressed(StaticData.moveLeft, (key) -> {
            if(i == 1) {
                if (!player.isAttacking() || player.isInAir()) {
                    player.setDirectionLR(0);
                    if(!player.isInAir()) {
                        player.setLookingAt(0);
                        send("LOOKING" + playerNumber + "#" + player.getLookingAt());
                    }
                    player.setDecelerating(false);
                }
                i = 0;
            }
        });
        Input.keyboard().onKeyReleased(StaticData.moveLeft, (key) -> {
            if(i == 1) {
                player.setDecelerating(true);
                i = 0;
            }
        });
        Input.keyboard().onKeyPressed(StaticData.moveRight, (key) -> {
            if(i == 1) {
                if (!player.isAttacking() || player.isInAir()) {
                    player.setDirectionLR(1);
                    if(!player.isInAir()) {
                        player.setLookingAt(1);
                        send("LOOKING" + playerNumber + "#" + player.getLookingAt());
                    }
                    player.setMoving(false);
                }
                i = 0;
            }
        });
        Input.keyboard().onKeyReleased(StaticData.moveRight, (key) -> {
            if(i == 1) {
                player.setDecelerating(true);
                i = 2;
            }
        });
        Input.keyboard().onKeyPressed(StaticData.moveUp, (key) -> player.setDirectionUD(0));
        Input.keyboard().onKeyReleased(StaticData.moveUp, (key) -> player.setDirectionUD(-1));
        Input.keyboard().onKeyPressed(StaticData.moveDown, (key) -> player.setDirectionUD(1));
        Input.keyboard().onKeyReleased(StaticData.moveDown, (key) -> player.setDirectionUD(-1));
    }

    /**
     * Methode, die die Inputs für die Angriffe verarbeitet
     */
    private void processInputsAttacks(){
        Input.keyboard().onKeyTyped(StaticData.normalAttack, (key) -> {
            if (player.getAttackWindDown() <= 0) {
                //player.setHorizontalSpeed(0);
                player.setDecelerating(true);
                if (player.getDirectionLR() != -1) {
                    if(player instanceof Gambler) {
                        send("ATTACK" + playerNumber + "#nAR#"+((Gambler) player).getResult());
                    }else{
                        send("ATTACK" + playerNumber + "#nAR");
                    }
                    player.normalAttackRun();
                } else if (player.getDirectionUD() == 1) {
                    if(player instanceof Gambler){
                        send("ATTACK"+playerNumber+"#nAD#"+((Gambler) player).getResult());
                    }else{
                        send("ATTACK"+playerNumber+"#nAD");
                    }
                    player.normalAttackDown();
                } else if (player.getDirectionUD() == 0) {
                    if(player instanceof Gambler){
                        send("ATTACK"+playerNumber+"#nAU#"+((Gambler) player).getResult());
                    }else{
                        send("ATTACK"+playerNumber+"#nAU");
                    }
                    player.normalAttackUp();
                } else {
                    if(player instanceof Gambler){
                        send("ATTACK"+playerNumber+"#nAS#"+((Gambler) player).getResult());
                    }else{
                        send("ATTACK"+playerNumber+"#nAS");
                    }
                    player.normalAttackStand();
                }
            }
        });
        Input.keyboard().onKeyTyped(StaticData.specialAttack, (key) -> {
            if (player.getAttackWindDown() <= 0) {
                //player.setHorizontalSpeed(0);
                player.setDecelerating(true);
                if (player.getAttackWindDown() <= 0) {
                    player.setHorizontalSpeed(0);
                    if (player.getDirectionUD() == 0) {
                        if(player instanceof Gambler){
                            send("ATTACK"+playerNumber+"#sAU#"+((Gambler) player).getResult());
                        }else{
                            send("ATTACK"+playerNumber+"#sAU");
                        }
                        player.specialAttackUp();
                    } else if (player.getDirectionLR() != -1) {
                        player.specialAttackRun();
                        send("ATTACK"+playerNumber+"#sAR");
                    } else if (player.getDirectionUD() == 1) {
                        player.specialAttackDown();
                        send("ATTACK"+playerNumber+"#sAD");
                    } else {
                        player.specialAttackStand();
                        send("ATTACK"+playerNumber+"#sAS");
                    }
                }
            }
        });
    }

    /**
     * Methode, die den Input für den Jump verarbeitet
     */
    private void processInputsJump(){
        Input.keyboard().onKeyTyped(StaticData.jump, (key) -> {
            if(player.getAttackWindDown() <= 0){
                if(player.getJumpsAvailable() > 0 && player.getJumpCooldown() <= 0){
                    send("JUMP"+playerNumber);
                    player.setVerticalSpeed(-700);
                    player.setInAir(true);
                    player.setJumpsAvailable(player.getJumpsAvailable()-1);
                    player.setJumpCooldown(0.5);
                }
            }
        });
    }

}
