package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.*;
import model.Maps.Map1;
import model.Screens.IngameScreen;

import java.util.ArrayList;

public class GameController {

    private PhysicsController physicsController;

    public GameController() {

        int switchFunction = 2;

        if(switchFunction == 1) {
            User user = new User();
            user.hostGame(2796);


            //user.init();


        }else if (switchFunction == 2){
            User user2 = new User();
            user2.joinGame("localhost", 2796);
            //user2.init();

        }


        physicsController = new PhysicsController();

        Game.getEnvironment().add(new Timer());
        CollisionController collisionController = new CollisionController(this);
        Game.getRenderEngine().setBaseRenderScale(1);

        //IMap map = Game.getEnvironment().getMap();



        System.out.println(getPlayers().size());


    }

    public ArrayList<GravitationalObject> getGravObjects(){
        return physicsController.getGravObjects();
    }

    public ArrayList<Player> getPlayers(){
        return physicsController.getPlayers();
    }
}
