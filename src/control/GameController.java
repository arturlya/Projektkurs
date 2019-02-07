package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.graphics.RenderType;
import model.*;
import model.Maps.Map1;
import model.Screens.IngameScreen;

import java.util.ArrayList;

/**
 * Klasse GameController
 * Wird zu Anfang des Spiels
 */
public class GameController {

    private PhysicsController physicsController;
    private CollisionController collisionController;
    private Timer timer;

    /**
     * Konstruktor des GameControllers
     * Erstellt PhysicsController, Timer und CollisionController
     */
    public GameController() {
        physicsController = new PhysicsController();
        timer = new Timer();
        collisionController = new CollisionController(physicsController);
        Game.getRenderEngine().setBaseRenderScale(1);
    }

    /**
     * Entfernt die Controller vom Spielfluss
     */
    public void removeControllers(){
        if(physicsController != null)
        Game.getLoop().detach(physicsController);
        if(timer != null)
        Game.getLoop().detach(timer);
        if(collisionController != null)
        Game.getLoop().detach(collisionController);
    }
}
