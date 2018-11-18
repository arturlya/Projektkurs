package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.GravitationalObject;
import model.Mage;
import model.Maps.Map1;
import model.Screens.IngameScreen;
import model.User;
import model.Warrior;

import java.util.ArrayList;

public class GameController {

    private PhysicsController physicsController;

    public GameController(IngameScreen ingameScreen) {

        physicsController = new PhysicsController();

        Game.getEnvironment().add(new Timer());
        CollisionController collisionController = new CollisionController(this);
        Game.getRenderEngine().setBaseRenderScale(1);

        //IMap map = Game.getEnvironment().getMap();

        Mage m = new Mage(true);
        m.setX(300);
        m.setY(100);
        Game.getEnvironment().add(m);
        Game.getEnvironment().add(m,RenderType.NORMAL);

        Warrior w = new Warrior(false);
        w.setX(400);
        w.setY(100);
        Game.getEnvironment().add(w);
        Game.getEnvironment().add(w,RenderType.NORMAL);


/*
        int switchFunction = 2;

        if(switchFunction == 1) {
            User user = new User();
            user.hostGame(2796);
            user.init();


        }else if (switchFunction == 2){
            User user2 = new User();
            user2.joinGame("localhost", 2796);
            user2.init();

        }*/
    }

    public ArrayList<GravitationalObject> getGravObjects(){
        return physicsController.getGravObjects();
    }
}
