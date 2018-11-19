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

    /**
     * Nutzer der Anwendung.
     * Kann einen Server inklusive eigenen Client erstellen oder einem anderem Server beitreten.
     */
    public User(){
        super(0,0);
        clientActive = false;
    }

    /**
     * Methode zur Initialisierung
     */
    public void init(){
        client.init();
    }

    /**
     * Der Nutzer erstellt einen neuen Client, mit dem er einem Server beitritt.
     *
     * @param ip IP des Servers
     * @param port Port des Servers
     */
    public void joinGame(String ip, int port){
        client = new GameClient(ip,port);
        if(client.isConnected()) {
            clientActive = true;
            System.out.println("joined " + ip + " at joinPort " + port);
        }else{
            System.out.println("couldn't connect to "+ip);
        }
    }

    /**
     * Der Nutzer erstellt einen GameServer den er mit seinem Client betritt.
     *
     * @param port Port des neuen Servers
     */
    public void hostGame(int port){
        this.port = port;
        server = new GameServer(port);

        client = new GameClient("localhost",port);

        clientActive = true;
    }

    /**
     * Der Nutzer verlässt den aktuellen Server.
     */
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
