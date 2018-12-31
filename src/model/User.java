package model;

import control.PhysicsController;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import model.Screens.IngameScreen;

import java.awt.event.MouseEvent;

public class User extends GuiComponent {

    private GameClient client;
   // private GameServer server;
    private ConnectionClient connector;

    private int port;
    private boolean clientActive;
    private int cursorX,cursorY;

    /**
     * Nutzer der Anwendung.
     * Kann einen Server inklusive eigenen Client erstellen oder einem anderem Server beitreten.
     */
    public User(){
        super(0,0);
        clientActive = false;
        connector = new ConnectionClient(this,7567);
        // connector = new ConnectionClient(this,2567);

    }


    /**
     * Methode zur Initialisierung
     */
    public void init(){
//        client.init();
    }

    /**
     * Der Nutzer erstellt einen neuen Client, mit dem er einem Server beitritt.
     *
     * @param ip IP des Servers
     * @param port Port des Servers
     */
    public void joinGame(String ip, int port){
        System.out.println("Connecting to "+ip);
        client = new GameClient(ip,port);
        if(client.isConnected()) {
            clientActive = true;
            System.out.println("joined " + ip + " at joinPort " + port);
        }else{
            System.out.println("couldn't connect to "+ip);
        }
    }


    /**
     * Der Nutzer verlässt den aktuellen Server.
     */
    public void closeConnection(){
        if(clientActive){
            client.close();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        //System.out.println(1);
    }

    public boolean isConnected(){
        if (client != null) {
            return client.isConnected();
        }else return false;
    }
}
