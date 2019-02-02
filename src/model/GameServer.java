package model;

import model.abitur.netz.Server;

/**
 * Klasse GameServer.
 * Arbeitet mit der Klasse GameClient zusammen.
 * Ist für den Austausch der Daten in einer Spielsitzung zuständig.
 */
public class GameServer extends Server {

    /** Specihert die aktuelle Anzahl an Clients*/
    private int numberOfPlayers;
    /** Speichert die maximale Anzahl an Clients*/
    private int maxPlayers;
    /** Speichert die Anzahl der Spieler, die bereit sind*/
    private int startVote;

    /** Speichert IP und Port aller Clients*/
    private String[][] users;

    /** Speichert alle Spieler mit ihrer Nummer und Figur*/
    private String allPlayer = "ALL";

    /**
     * Konstruktor der Klasse GameServer.
     *
     * @param port Port des Servers
     */
    public GameServer(int port){
        super(port);
        startVote = 0;
        maxPlayers = 4;
        users = new String[maxPlayers][2];
    }

    /**
     * Methode, die bei neuer Verbindung aufgerufen wird.
     *
     * @param pClientIP IP des beigetretenen Clients
     * @param pClientPort Port des beigetretenen Clients
     */
    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
        numberOfPlayers++;
        if(numberOfPlayers ==1){
            System.out.println("Waiting for Players");
            send(pClientIP,pClientPort,"NUMBER"+numberOfPlayers);
            users[numberOfPlayers-1][0] = pClientIP;
            users[numberOfPlayers-1][1] = Integer.toString(pClientPort);
        }else if(numberOfPlayers <= maxPlayers){
            System.out.println("Waiting for start");
            send(pClientIP,pClientPort,"NUMBER"+numberOfPlayers);
            users[numberOfPlayers-1][0] = pClientIP;
            users[numberOfPlayers-1][1] = Integer.toString(pClientPort);
        }else{
            System.out.println("Server is already full");
        }
    }

    /**
     * Verarbeitet die Nachrichten der Clients.
     *
     * @param pClientIP IP des Clients
     * @param pClientPort Port des Clients
     * @param pMessage Nachricht des Clients.
     *                 - Bei "START" wertet der Server den Vote des Clients aus
     *                 - bei "PLAYER" merkt er sich die Figur des Spielers
     *                 - bei allen anderen leitet er die Nachricht an die Client nur weiter
     */
    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        if (pMessage.contains("START") && numberOfPlayers > 1) {
            String[] temp = pMessage.split("START", 2);
            if (temp[1].contains("true")) {
                startVote++;
                System.out.println("Player ready");
            } else if (temp[1].contains("false")) {
                startVote--;
                System.out.println("Player must prepare");
            } else {
                System.err.println("Couldn't handle : " + temp[1]);
            }
        } else if (pMessage.contains("PLAYER")) {

            String[] temp = pMessage.split("PLAYER", 3);
            switch (Integer.parseInt(temp[2])) {
                case 1:
                    if(numberOfPlayers>1) {
                        String tmp[] = allPlayer.split("NEXT");
                        allPlayer = "ALL" + numberOfPlayers + "NEXT";
                        for(int i=1;i<tmp.length;i++){
                            if(tmp[i] != null) {
                                allPlayer = allPlayer + tmp[i];
                                allPlayer = allPlayer +"NEXT";
                            }
                        }
                        allPlayer = allPlayer + temp[1] + "#"+"warrior";
                    }else{ allPlayer = "ALL"+numberOfPlayers+"NEXT"+temp[1]+"#"+"warrior";}
                    break;
                case 2:
                    if(numberOfPlayers>1) {
                        String tmp[] = allPlayer.split("NEXT");
                        allPlayer = "ALL" + numberOfPlayers + "NEXT";
                        for(int i=1;i<tmp.length;i++){
                            if(tmp[i] != null) {
                                allPlayer = allPlayer + tmp[i];
                                allPlayer = allPlayer +"NEXT";
                            }
                        }
                        allPlayer = allPlayer + temp[1] + "#"+"Mage";
                    }else{ allPlayer = "ALL"+numberOfPlayers+"NEXT"+temp[1]+"#"+"Mage";}
                    break;
                case 3:
                    if(numberOfPlayers>1) {
                        String tmp[] = allPlayer.split("NEXT");
                        allPlayer = "ALL" + numberOfPlayers + "NEXT";
                        for(int i=1;i<tmp.length;i++){
                            if(tmp[i] != null) {
                                allPlayer = allPlayer + tmp[i];
                                allPlayer = allPlayer +"NEXT";
                            }
                        }
                        allPlayer = allPlayer + temp[1] + "#"+"Gambler";
                    }else{ allPlayer = "ALL"+numberOfPlayers+"NEXT"+temp[1]+"#"+"Gambler";}
                    break;
            }
            sendToAll(allPlayer);
        } else if (pMessage.contains("POSITION")) {
            sendToAll(pMessage);
        }else if(pMessage.contains("ATTACK")){
            sendToAll(pMessage);
        } else if (pMessage.contains("JUMP")) {
            sendToAll(pMessage);
        }else if(pMessage.contains("LOOKING")){
            sendToAll(pMessage);
        }

        if(isStarting() && startVote<10){
            sendToAll("PLAYER");
            sendToAll(allPlayer);
            sendToAll("STARTtrue");
            startVote = Integer.MAX_VALUE;
            System.out.println("Game is starting");
        }

    }

    /**
     * Verarbeitet das Verlassen eies Spielers und leitet das Verlassen an die restlichen Clients weiter.
     *
     * @param pClientIP IP des gegangenen Spielers
     * @param pClientPort Port des gegangenen Spielers
     */
    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {
        for(int i=0;i<users.length;i++){
            if(users[i][0] != null) {
                if (users[i][0].equals(pClientIP) && users[i][1].equals(Integer.toString(pClientPort))) {
                    sendToAll("QUIT" + (i + 1));
                    numberOfPlayers--;
                    System.out.println("Player " + (i + 1) + " left the game!");
                }
            }
        }
        if(numberOfPlayers <= 0) {
            close();
            System.out.println("Closing Server");
        }
    }

    /**
     * @return Gibt zurück, ob alle Spieler( müssen mehr als 1 sein) bereit sind.
     */
    private boolean isStarting(){
        if(startVote == numberOfPlayers && numberOfPlayers >1){
            return true;
        }else{
            return false;
        }
    }

}
