package model;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import model.Screens.MenuScreen;
import model.abitur.netz.Client;

/**
 * Klasse ConnectionClient.
 * Leitet die User über den ConnectionServer zu den Spielsitzungen weiter, ohne die IP zu benötigen.
 */
public class ConnectionClient extends Client implements IUpdateable {

    /** Merkt sich die IP der Spielsitzung, der man beitreten will*/
    private String serverIP;
    /** Merkt sich den Port der Spielsitzung, der man beitreten will*/
    private int serverPort;
    /** Nutzer, für den ein ConnectionClient erstellt wird, bzw. welcher in Spielsitzungen verbunden wird*/
    private User user;
    /** Merkt sich, ob der User mit einer Sielsitzung verbunden ist*/
    private boolean connected;
    /** Merkt sich, ob bereits an Anfrage zum Beitritt eines Spiels gesendet wurde*/
    private boolean requestSent;

    /**
     * Konstruktor der Klasse ConnectionClient.
     * Verbindet sich mit dem ConnectionServer auf dem Raspberry.
     *
     * @param user Nutzer, für den ein ConnectionClient erstellt wird, bzw. welcher in Spielsitzungen verbunden wird
     * @param port Port des ConnectionServers
     */
    public ConnectionClient(User user,int port){
        super("localhost",port);
        //super("178.201.129.203",port);
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

    /**
     * Update des Interfaces IUpdateable
     */
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

    /**
     * Verarbeitet eine Nachricht vom ConnectionServer
     *
     * @param pMessage Nachricht des ConnectionServers
     */
    @Override
    public void processMessage(String pMessage) {
        if(pMessage.contains("SERVER") && !connected){
            String[] temp = pMessage.split("SERVER");
            temp = temp[1].split("#");

            //serverIP = temp[0];
                serverIP = "192.168.178.61";
            serverPort = Integer.parseInt(temp[1]);
            System.out.println("Joining "+serverIP+" at "+serverPort);
            user.joinGame(serverIP,serverPort);
            connected = true;
        }else if(pMessage.contains("USED")){
            MenuScreen.hostPort = "";
            MenuScreen.joinPort = "";
            requestSent = false;
        }
    }

    public void removeCurrentConnection(){
        requestSent = false;
        connected = false;
    }

}
