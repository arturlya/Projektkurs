package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import model.IngameScreen;
import model.StaticData;
import model.User;
import model.Warrior;

public class GameController {
    public GameController(de.gurkenlabs.litiengine.environment.Environment environment, IngameScreen ingameScreen) {

        Game.loadEnvironment(environment);
        Game.getEnvironment().add(new Timer());

        //IMap map = Game.getEnvironment().getMap();

        Warrior m = new Warrior(true);
        Game.getEnvironment().add(m);
        ingameScreen.addGravObject(m);

        int switchFunction = 2;

        if(switchFunction == 1) {
            User user = new User(ingameScreen);
            user.hostGame(2796);
            user.init();


        }else {
            User user2 = new User(ingameScreen);
            user2.joinGame("192.168.0.117", 2796);
            user2.init();

        }
    }
}
