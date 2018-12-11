package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import model.Screens.MenuScreen;
import model.abitur.netz.Client;

public class ConnectionClient extends Client implements IUpdateable {

    private String serverIP;
    private int serverPort;
    private boolean foundServer;
    private User user;
    private boolean connected;

    public ConnectionClient(User user,int port){
        super("192.168.178.50",port);
        Game.getLoop().attach(this);
        foundServer = false;
        this.user = user;
        connected = false;
        if(isConnected()) {
            System.out.println("Connected to ConnectionServer");
        }else{
            System.out.println("Not connected to ConnectionServer");
        }

    }

    @Override
    public void update() {
        if(MenuScreen.hostPort.length() == 4 && !connected){
            send("SERVER"+StaticData.ip+"#"+MenuScreen.hostPort);
            user.hostGame(Integer.parseInt(MenuScreen.hostPort));
            connected = true;
        }
        if(MenuScreen.joinPort.length() == 4){
            send("CONNECT"+MenuScreen.joinPort);
        }

    }

    @Override
    public void processMessage(String pMessage) {
        if(pMessage.contains("SERVER") && !connected){
            String[] temp = pMessage.split("SERVER");
            temp = temp[1].split("#");
            serverIP = temp[0];
            serverPort = Integer.parseInt(temp[1]);
            user.joinGame(serverIP,serverPort);
            connected = true;
            //foundServer = true;
        }
    }


}
