package model;

import model.abitur.netz.Server;

public class GameServer extends Server {

    private int numberOfPlayers;
    private int maxPlayers;
    private int startVote;

    private Player[] players;



    public GameServer(int port){
        super(port);
        startVote = 0;
        maxPlayers = 4;
        players = new Player[maxPlayers];
    }

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

    @Override
    public void processMessage(String pClientIP, int pClientPort, Object pMessage) {
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
                        break;
                    case 2:
                        players[Integer.parseInt(temp[1]) - 1] = new Mage(false);
                        break;
                }
                System.out.println("Sended all players");
                sendToAll(getAllPlayers());
            } else if (message.contains("POSITION")) {
                sendToAll(message);
                String[] temp = message.split("POSITION");
                String[] help = temp[1].split("#", 3);

                if (players[Integer.parseInt(help[0])] != null) {
                    players[Integer.parseInt(help[0])].setX(Double.parseDouble(help[1]));
                    players[Integer.parseInt(help[0])].setY(Double.parseDouble(help[2]));
                    System.out.println("Refreshing");
                }
            }
        }else if(pMessage.getClass().getName().equals("model.Player")){
            System.out.println("got Player");
            Player player = (Player) pMessage;
            players[player.getPlayerNumber()-1] = player;
        }
        if(isStarting()){

            sendToAll("STARTtrue");
        }

    }

    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {
        System.out.println("Player left the game");
        sendToAll("QUIT");
    }

    private boolean isStarting(){
        if(startVote == numberOfPlayers && numberOfPlayers >1){
            return true;
        }else{
            return false;
        }
    }

    private String getAllPlayers(){
        String temp = "ALL";
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
