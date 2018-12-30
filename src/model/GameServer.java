package model;

import model.abitur.netz.Server;

public class GameServer extends Server {

    private int numberOfPlayers;
    private int maxPlayers;
    private int startVote;

    private String[][] playerIP;


    private String allPlayer = "ALL";

    /**
     *
     * @param port Port des Servers
     */
    public GameServer(int port){
        super(port);
        startVote = 0;
        maxPlayers = 4;
        playerIP = new String[maxPlayers][2];
        System.out.println("Created new GameServer");
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
            playerIP[numberOfPlayers-1][0] = pClientIP;
            playerIP[numberOfPlayers-1][1] = Integer.toString(pClientPort);
        }else if(numberOfPlayers <= maxPlayers){
            System.out.println("Waiting for start");
            send(pClientIP,pClientPort,"PLAYER"+numberOfPlayers);
            playerIP[numberOfPlayers-1][0] = pClientIP;
            playerIP[numberOfPlayers-1][1] = Integer.toString(pClientPort);
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
     *                 - bei "POSITION" leitet er die Position eines bewegenden Spielers weiter
     *                 - bei "HURT" leitet er die HurtBox eines Spielers weiter
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
                    //    players[Integer.parseInt(temp[1]) ] = new Warrior(false);
                    //    players[Integer.parseInt(temp[1]) ].setPlayerNumber(Integer.parseInt(temp[1]));
                        if(numberOfPlayers>1) {
                            String tmp[] = allPlayer.split("NEXT");
                            allPlayer = "ALL" + numberOfPlayers + "NEXT";
                            for(int i=1;i<tmp.length;i++){
                                if(tmp[i] != null) {
                                    allPlayer = allPlayer + tmp[i];
                                    //if(i<numberOfPlayers-1){
                                        allPlayer = allPlayer +"NEXT";
                                   // }
                                }
                            }
                            allPlayer = allPlayer + numberOfPlayers + "#"+"Warrior";
                        }else{ allPlayer = "ALL"+numberOfPlayers+"NEXT"+numberOfPlayers+"#"+"Warrior";}
                        break;
                    case 2:
                    //    players[Integer.parseInt(temp[1]) - 1] = new Mage(false);
                    //    players[Integer.parseInt(temp[1]) - 1].setPlayerNumber(Integer.parseInt(temp[1]));
                        if(numberOfPlayers>1) {
                            String tmp[] = allPlayer.split("NEXT");
                            allPlayer = "ALL" + numberOfPlayers + "NEXT";
                            for(int i=1;i<tmp.length;i++){
                                if(tmp[i] != null) {
                                    allPlayer = allPlayer + tmp[i];
                                    //if(i<numberOfPlayers-1){
                                    allPlayer = allPlayer +"NEXT";
                                    // }
                                }
                            }
                            allPlayer = allPlayer + numberOfPlayers + "#"+"Mage";
                        }else{ allPlayer = "ALL"+numberOfPlayers+"NEXT"+numberOfPlayers+"#"+"Mage";}
                        break;
                }
                //System.out.println("Sended all players");
              //  sendToAll(getAllPlayers());


                sendToAll(allPlayer);

            } else if (pMessage.contains("POSITION")) {
                sendToAll(pMessage);
                String[] temp = pMessage.split("POSITION");
                String[] help = temp[1].split("#", 3);

               /* if (players[Integer.parseInt(help[0])-1] != null) {
                    players[Integer.parseInt(help[0])-1].setX(Double.parseDouble(help[1]));
                    players[Integer.parseInt(help[0])-1].setY(Double.parseDouble(help[2]));
                    //System.out.println("Refreshing");
                }*/
            }else if(pMessage.contains("ATTACK")){
                sendToAll(pMessage);
            } else if (pMessage.contains("JUMP")) {
                sendToAll(pMessage);
            }else if(pMessage.contains("LOOKING")){
                sendToAll(pMessage);
            }

        if(isStarting() && startVote<10){
/*
            if(players[0] != null && players[1] != null) {
                if(players[0].getPlayerNumber()!= 0 && players[1].getPlayerNumber() != 0) {
                    //sendToAll(getAllPlayers());
                    sendToAll(allPlayer);
                    sendToAll("STARTtrue");
                    startVote = Integer.MAX_VALUE;
                    System.out.println("Sended all players");
                }
            }else{
                System.out.println("Nicht alle haben einen Spieler gewählt");
                sendToAll("CHOOSE");
            }*/

            sendToAll(allPlayer);
            sendToAll("STARTtrue");
            startVote = Integer.MAX_VALUE;
            System.out.println("Game is starting");
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
        //System.out.println("Player left the game");
        System.out.println(""+pClientIP+" at port "+pClientPort+" lefted");

      /*  for(int i=0;i<playerIP.length;i++){
            for(int j=0;j<playerIP[i].length;j++){
                System.out.println(playerIP[i][j]);
            }
            System.out.println("~~~~~");
        }
*/
        for(int i=0;i<playerIP.length;i++){
            if(playerIP[i][0] != null) {
                if (playerIP[i][0].equals(pClientIP) && playerIP[i][1].equals(Integer.toString(pClientPort))) {
                    sendToAll("QUIT" + (i + 1));
                    System.out.println("QUIT"+(i+1));
                    System.out.println("Player " + (i + 1) + " left the game!");
                }
            }
        }


        //sendToAll("QUIT");
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
