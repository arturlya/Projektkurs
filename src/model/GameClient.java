package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.input.Input;
import model.abitur.datenstrukturen.List;
import model.abitur.netz.Client;

import java.awt.event.KeyEvent;

public class GameClient extends Client implements IUpdateable {

    private Player player;
    private boolean ready;

    public GameClient(String ip, int port){
        super(ip,port);
        ready = false;
        choosePlayer(1);
    }

    @Override
    public void update(){
        if(player != null) {
            Input.keyboard().onKeyPressed(KeyEvent.VK_ENTER, (key) -> {
                send("STARTtrue");
                System.out.println("sended true");
                ready = true;
            });
        }
        if(ready){
            Input.keyboard().onKeyPressed(KeyEvent.VK_ENTER,(key)->{
                send("STARTfalse");
                System.out.println("sended false");
                ready = false;
            });
        }
    }

    @Override
    public void processMessage(String pMessage) {

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
