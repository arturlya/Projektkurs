package model;


public class User extends model.abitur.netz.Client {

    public User(int port){
        super(StaticData.getIp(),port);

    }

    @Override
    public void processMessage(String pMessage) {

    }

}
