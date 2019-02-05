package model;

import de.gurkenlabs.litiengine.gui.GuiComponent;

import java.awt.event.MouseEvent;

/**
 * Klasse User.
 * Besitzt die beiden Clients um sich mit Servern zu verbinden.
 */
public class User extends GuiComponent {

    /** Client für eine Spielsitzung*/
    private GameClient client;
    /** Client für das Finden einer Spielsitzung beim Raspberry*/
    private ConnectionClient connector;

    /** Speichert, ob der GameClient verbunden ist*/
    private boolean clientActive;

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

    /**
     * mouseMoved der Oberklasse GuiComponent
     *
     * @param e Das MouseEvent zum Interagieren
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
    }

    /**
     * @return Gibt zurück, ob der User mt einer Spielsitzung verbunden ist.
     */
    public boolean isConnected(){
        if (client != null) {
            return client.isConnected();
        }else return false;
    }
}
