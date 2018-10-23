package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.input.Input;
import model.abitur.datenstrukturen.List;
import model.abitur.netz.Client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameClient extends Client implements IUpdateable {

    private Player player;
    private boolean ready;
    private boolean gameStarted;

    private List<Player> others;
    private IngameScreen ingameScreen;

    public GameClient(String ip, int port, IngameScreen ingameScreen){
        super(ip,port);
        ready = false;
        others = new List<>();
        this.ingameScreen = ingameScreen;
        choosePlayer(1);
        Input.getLoop().attach(this);
        Input.keyboard().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if (ready) {
                        send("STARTfalse");
                        ready = false;
                    }else {
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

    @Override
    public void update(){
        if(player != null)
        if (gameStarted && player.isMoving()) {
            send("POSITION"+player.getPlayerNumber()+"#" + player.getX() + "#" + player.getY());
            //send((Player) player);
        }
    }





    @Override
    public void processMessage(Object pMessage) {
        int max = 0;
        if(max == 0) {
            if(pMessage.getClass().getName().equals("java.lang.String")) {
                String message = pMessage.toString();
                if (message.contains("START")) {
                    String[] temp = message.split("START", 2);
                    if (temp[1].contains("true")) {
                        gameStarted = true;
                    } else {
                        gameStarted = false;
                    }
                } else if (message.contains("PLAYER")) {
                    String[] temp = message.split("PLAYER");
                    if (temp[1] != null && player != null) {
                        player.setPlayerNumber(Integer.parseInt(temp[1]));
                    }
                    choosePlayer(1);
                } else if (message.contains("ALL")) {
                    String[] temp = message.split("ALL");
                    temp = temp[1].split("NEXT");
                    for (int i = 0; i < temp.length; i++) {
                        String[] charInfo = temp[i].split("#", 2);
                        if (Integer.parseInt(charInfo[0]) != player.getPlayerNumber()) {
                            if (charInfo[1].equals("Warrior")) {
                                Player otherPlayer = new Warrior(false);
                                others.append(otherPlayer);
                                Game.getEnvironment().add(otherPlayer);
                                ingameScreen.addGravObject(otherPlayer);
                            } else if (charInfo[1].equals("Mage")) {
                                Player otherPlayer = new Mage(false);
                                others.append(otherPlayer);
                                Game.getEnvironment().add(otherPlayer);
                                ingameScreen.addGravObject(otherPlayer);
                            }
                        }
                    }

                } else if (message.contains("POSITION")) {
                    String[] temp = message.split("POSITION");
                    temp = temp[1].split("#");
                    if (Integer.parseInt(temp[0]) != player.getPlayerNumber()) {

                        int posInList = Integer.parseInt(temp[0]) - 1;
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
            }

            max++;
        }
    }


    protected void init(){
        List<IEntity> entityList = new List<>();
        entityList.append(player);
        entityList.toFirst();
        while(entityList.hasAccess()){
            Game.getEnvironment().add(entityList.getContent());
            if(entityList.getContent() instanceof GravitationalObject){
                ingameScreen.addGravObject((GravitationalObject)(entityList.getContent()));
            }
            entityList.next();
        }
    }

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
