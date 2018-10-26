package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.gui.screens.Resolution;
import model.IngameScreen;
import model.StaticData;

import java.awt.*;
import java.util.List;

public class MainController {

    public static void main(String[] args){
        new MainController();
    }

    private IngameScreen ingameScreen;
    private Environment environment;

    public MainController(){
        Game.init();
        Game.load("assets/maps/game.litidata");
        Game.getEnvironment().getMap().setName("Map1");
        environment = new Environment(Game.getEnvironment().getMap());
        ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
        Game.getConfiguration().graphics().setFullscreen(false);
        GameController gameController = new GameController(environment,ingameScreen);
        Game.start();
    }
}
