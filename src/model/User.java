package model;


import model.abitur.netz.Client;

public class User {

    private GameClient client;
    private GameServer server;

    private int port;
    private boolean clientActive;

    public User(){
        clientActive = false;
    }

    public void init(IngameScreen ingameScreen){
        client.init(ingameScreen);
    }
    public void joinGame(String ip, int port){
        client = new GameClient(ip,port);
        clientActive = true;
    }
    public void hostGame(int port){
        this.port = port;
        server = new GameServer(port);
        client = new GameClient(StaticData.getIp(),port);
        clientActive = true;
    }

    public GameClient getClient(){
        if(clientActive){
            return client;
        }
        return null;
    }



}
