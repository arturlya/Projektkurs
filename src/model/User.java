package model;

import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.awt.*;
import java.awt.event.MouseEvent;

public class User extends GuiComponent {

    private GameClient client;
    private GameServer server;
    private IngameScreen ingameScreen;

    private int port;
    private boolean clientActive;
    private int cursorX,cursorY;

    public User(IngameScreen ingameScreen){
        super(0,0);
        clientActive = false;
        this.ingameScreen = ingameScreen;
    }

    public void init(){
        client.init();
    }

    public void joinGame(String ip, int port){
        client = new GameClient(ip,port,ingameScreen);
        if(client.isConnected()) {
            clientActive = true;
            System.out.println("joined " + ip + " at port " + port);
        }else{
            System.out.println("couldn't connect to "+ip);
        }
    }

    public void hostGame(int port){
        this.port = port;
        server = new GameServer(port);

        client = new GameClient("localhost",port,ingameScreen);

        clientActive = true;
    }

    public void closeConnection(){
        if(clientActive){
            client.close();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);

    }
}
