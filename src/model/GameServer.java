package model;

import model.abitur.netz.Server;

public class GameServer extends Server {

    private int numberOfPlayers;
    private int maxPlayers;
    private int startVote;


    public GameServer(int port){
        super(port);
        startVote = 0;
        maxPlayers = 4;
    }

    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
        numberOfPlayers++;
        if(numberOfPlayers ==1){
            System.out.println("Waiting for Players");
        }else if(numberOfPlayers <= maxPlayers){
            System.out.println("Waiting for start");
        }else{
            System.out.println("Server is already full");
        }

    }

    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        if(pMessage.contains("START")){
            String[] temp = pMessage.split("START",1);
            if(temp[1].contains("true")){
                startVote++;
                System.out.println("Player ready");
            }else if(temp[1].contains("false")){
                startVote--;
                System.out.println("Player must prepare");
            }else{
                System.err.println("Couldn't handle : "+temp[1]);
            }
            sendToAll("START"+startVote);
        }
    }

    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {

    }
}
