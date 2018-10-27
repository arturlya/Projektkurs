package control;

import de.gurkenlabs.litiengine.Game;
import model.Screens.IngameScreen;
import model.User;

public class GameController {
    public GameController(de.gurkenlabs.litiengine.environment.Environment environment, IngameScreen ingameScreen) {

        Game.getEnvironment().add(new Timer());
        Game.getEnvironment().add(new PhysicsController(ingameScreen.getGravObjects()));

        //IMap map = Game.getEnvironment().getMap();

        /*Warrior m = new Warrior(true);
        Game.getEnvironment().add(m);
        ingameScreen.addGravObject(m);*/

        int switchFunction = 2;

        if(switchFunction == 1) {
            User user = new User();
            user.hostGame(2796);
            user.init();


        }else if (switchFunction == 2){
            User user2 = new User();
            user2.joinGame("localhost", 2796);
            user2.init();

        }
    }
}
