package model;

import de.gurkenlabs.litiengine.gui.GuiComponent;
import model.Screens.IngameScreen;

import java.awt.event.MouseEvent;

public class User extends GuiComponent {

    private GameClient client;
    private GameServer server;

    private int port;
    private boolean clientActive;
    private int cursorX,cursorY;

    public User(){
        super(0,0);
        clientActive = false;
    }

    public void init(){
        client.init();
    }

    public void joinGame(String ip, int port){
        client = new GameClient(ip,port);
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

        client = new GameClient("localhost",port);

        clientActive = true;
    }

    public void closeConnection(){
        if(clientActive){
            client.close();
        }
    }

    /*@Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        System.out.println(1);
        if (e.getX() >= 645*1920/Game.getConfiguration().graphics().getResolutionWidth() && e.getX() <= 1270*1920/Game.getConfiguration().graphics().getResolutionWidth()) {
            System.out.println(1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(1);
    }*/

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        System.out.println(1);
    }
}
