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


        physicsController = new PhysicsController();

        Game.getEnvironment().add(new Timer());
        CollisionController collisionController = new CollisionController(physicsController);
        Game.getRenderEngine().setBaseRenderScale(1);
        System.out.println(getPlayers().size());


    }

    public ArrayList<GravitationalObject> getGravObjects(){
        return physicsController.getGravObjects();
    }

    public ArrayList<Player> getPlayers(){
        return physicsController.getPlayers();
    }
}
