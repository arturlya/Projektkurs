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
        if (gameStarted && player.isMoving()) {
            send("POSITION"+player.getPlayerNumber()+"#" + player.getX() + "#" + player.getY());
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
                        if (Integer.parseInt(charInfo[0]) != player.getPlayerNumber()) {
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
                                    Player otherPlayer = new Warrior(false);
                                    otherPlayer.setPlayerNumber(Integer.parseInt(charInfo[0]));
                                    others.append(otherPlayer);
                                    Game.getEnvironment().add(otherPlayer);
                                    Game.getEnvironment().add(otherPlayer, RenderType.NORMAL);
                                    //ingameScreen.addGravObject(otherPlayer);
                                    System.out.println("Added player");
                                } else if (charInfo[1].equals("Mage")) {
                                    Player otherPlayer = new Mage(false);
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
                    player = new Warrior(true);
                    break;
                case 2:
                    player = new Mage(true);


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
