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
        Game.getConfiguration().graphics().setFullscreen(true);
        Game.load("assets/maps/game.litidata");
        List<IMap> list = Game.getMaps();
        environment = new Environment(list.get(0));
        ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
        GameController gameController = new GameController(environment,ingameScreen);
        Game.start();
    }
}
