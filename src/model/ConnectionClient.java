package model;

import com.dosse.upnp.UPnP;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import model.Screens.MenuScreen;
import model.abitur.netz.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionClient extends Client implements IUpdateable {

    private String serverIP;
    private int serverPort;
    private boolean foundServer;
    private User user;
    private boolean connected;

    public ConnectionClient(User user,int port){
        super("localhost",port);
        // super("178.201.129.203",port);
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
            send("SERVER"+MenuScreen.hostPort);
           // user.hostGame(Integer.parseInt(MenuScreen.hostPort));
            //user.joinGame(getExternalIP(),Integer.parseInt(MenuScreen.hostPort));
            //connected = true;
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
            //serverIP = "178.201.129.203";
            serverPort = Integer.parseInt(temp[1]);
            System.out.println("Joining "+serverIP+" at "+serverPort);
            user.joinGame(serverIP,serverPort);
            connected = true;
            //foundServer = true;
        }
    }


    public String getExternalIP(){
        String ip = "";
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            ip = in.readLine(); //you get the IP as a String
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ip;
    }

}
