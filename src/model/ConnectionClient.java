package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import model.Screens.MenuScreen;
import model.abitur.netz.Client;

public class ConnectionClient extends Client implements IUpdateable {

    private String serverIP;
    private int serverPort;
    private User user;
    private boolean connected;
    private boolean requestSent;

    public ConnectionClient(User user,int port){
        //super("localhost",port);
        super("178.201.129.203",port);
        Game.getLoop().attach(this);
        this.user = user;
        connected = false;
        requestSent = false;
        if(isConnected()) {
            System.out.println("Connected to ConnectionServer");
        }else{
            System.out.println("Not connected to ConnectionServer");
        }

    }

    @Override
    public void update() {
        if(MenuScreen.hostPort.length() == 4 && !connected && !requestSent){
            send("SERVER"+MenuScreen.hostPort);
            requestSent = true;
        }
        if(MenuScreen.joinPort.length() == 4){
            send("CONNECT"+MenuScreen.joinPort);
        }
        if(connected && requestSent){
            requestSent = false;
        }

    }

    @Override
    public void processMessage(String pMessage) {
        if(pMessage.contains("SERVER") && !connected){
            String[] temp = pMessage.split("SERVER");
            temp = temp[1].split("#");

            serverIP = temp[0];
              //  serverIP = "192.168.178.61";
            serverPort = Integer.parseInt(temp[1]);
            System.out.println("Joining "+serverIP+" at "+serverPort);
            user.joinGame(serverIP,serverPort);
            connected = true;
        }
    }


}
