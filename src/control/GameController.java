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

        User user = new User();
        user.hostGame(4762);
        user.init(ingameScreen);

        User user2 = new User();
        user.joinGame(StaticData.getIp(),4762);
        user.init(ingameScreen);
    }
}
