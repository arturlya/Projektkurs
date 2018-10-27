package model;

import model.abitur.netz.Server;

public class GameServer extends Server {

    private int numberOfPlayers;
    private int maxPlayers;
    private int startVote;

    private Player[] players;


    /**
     *
     * @param port Port des Servers
     */
    public GameServer(int port){
        super(port);
        startVote = 0;
        maxPlayers = 4;
        players = new Player[maxPlayers];
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
            send(pClientIP,pClientPort,"PLAYER"+numberOfPlayers);
        }else if(numberOfPlayers <= maxPlayers){
            System.out.println("Waiting for start");
            send(pClientIP,pClientPort,"PLAYER"+numberOfPlayers);
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
     *                 - bei "PLAYER" merkt er sich den neuen Spieler und gibt ihm eine Spielernummer
     *                 - bei "POSITION" aktualisiert er die Position des bewegenden Spielers und leitet diese Nachricht weiter
     */
    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        if(pMessage.getClass().getName().equals("java.lang.String")) {
            String message = pMessage.toString();
            if (message.contains("START") && numberOfPlayers > 1) {
                String[] temp = message.split("START", 2);
                if (temp[1].contains("true")) {
                    startVote++;
                    System.out.println("Player ready");
                } else if (temp[1].contains("false")) {
                    startVote--;
                    System.out.println("Player must prepare");
                } else {
                    System.err.println("Couldn't handle : " + temp[1]);
                }
            } else if (message.contains("PLAYER")) {

                String[] temp = message.split("PLAYER", 3);
                switch (Integer.parseInt(temp[2])) {
                    case 1:
                        players[Integer.parseInt(temp[1]) - 1] = new Warrior(false);
                        players[Integer.parseInt(temp[1]) - 1].setPlayerNumber(Integer.parseInt(temp[1]));
                        break;
                    case 2:
                        players[Integer.parseInt(temp[1]) - 1] = new Mage(false);
                        players[Integer.parseInt(temp[1]) - 1].setPlayerNumber(Integer.parseInt(temp[1]));
                        break;
                }
                System.out.println("Sended all players");
                sendToAll(getAllPlayers());


            } else if (message.contains("POSITION")) {
                sendToAll(message);
                String[] temp = message.split("POSITION");
                String[] help = temp[1].split("#", 3);

                if (players[Integer.parseInt(help[0])-1] != null) {
                    players[Integer.parseInt(help[0])-1].setX(Double.parseDouble(help[1]));
                    players[Integer.parseInt(help[0])-1].setY(Double.parseDouble(help[2]));
                    //System.out.println("Refreshing");
                }
            }
        }
        if(isStarting() && startVote<10){

            if(players[0] != null && players[1] != null) {
                sendToAll("STARTtrue");
                sendToAll(getAllPlayers());
                startVote = Integer.MAX_VALUE;
                System.out.println("Sended all players");
            }else{
                System.out.println("Nicht alle haben einen Spieler gewählt");
                sendToAll("CHOOSE");
            }
            //System.out.println("Game is starting");
        }

    }

    /**
     * Verarbeitet das Verlassen eies Spielers
     *
     * @param pClientIP IP des gegangenen Spielers
     * @param pClientPort Port des gegangenen Spielers
     */
    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {
        System.out.println("Player left the game");
        sendToAll("QUIT");
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

    /**
     * @return Gibt einen String mit allen Spieler Unterklassen der einzelnen Clients mit dem Schlüsselwort "ALL"
     */
    private String getAllPlayers(){
        String temp = "ALL"+numberOfPlayers+"NEXT";
        for(int i=0;i<players.length;i++){
            if(players[i] != null){
                temp = temp+(i+1)+"#"+players[i].getName();
                if(i != numberOfPlayers-1)
                    temp = temp+"NEXT";
            }
        }
        return temp;
    }
}
