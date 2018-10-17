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

    public GameClient(String ip, int port){
        super(ip,port);
        ready = false;
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
        if (player.isMoving() && gameStarted) {
            send("" + player.getX() + "#" + player.getY());
        }
    }





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
            }
            max++;
        }
    }


    protected void init( IngameScreen ingameScreen){
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
        switch(number){
            case 1:
                player = new Warrior();
                break;
            case 2:
                player = new Mage();
                break;
        }
    }
}
