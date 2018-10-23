package control;

import de.gurkenlabs.litiengine.Game;
import model.IngameScreen;
import model.StaticData;
import model.User;
import model.Warrior;

public class GameController {
    public GameController(de.gurkenlabs.litiengine.environment.Environment environment, IngameScreen ingameScreen) {

        Game.loadEnvironment(environment);
        Game.getEnvironment().add(new Timer());


/*
        Warrior m = new Warrior();

        Game.getEnvironment().add(m);
        ingameScreen.addGravObject(m);*/

        User user = new User(ingameScreen);
        user.hostGame(2796);
        user.init();



//        User user2 = new User(ingameScreen);
  //      user2.joinGame("192.168.178.61",2796);
    //    user2.init();

    }
}
