package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.GravitationalObject;
import model.Screens.IngameScreen;
import model.User;
import model.Warrior;

public class GameController {
    public GameController(IngameScreen ingameScreen) {

        Game.getEnvironment().add(new Timer());
        Game.getEnvironment().add(new PhysicsController());

        //IMap map = Game.getEnvironment().getMap();
/*
        Warrior m = new Warrior(true);
        m.setX(0);
        m.setY(100);
        Game.getEnvironment().add(m);
        Game.getEnvironment().add(m,RenderType.NORMAL);
*/
        //ingameScreen.addGravObject(m);


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
