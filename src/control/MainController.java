package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.gui.screens.Resolution;
import model.IngameScreen;
import model.StaticData;

public class MainController {

    public static void main(String[] args){
        new MainController();
    }

    private IngameScreen ingameScreen;
    private Environment environment;

    public MainController(){
        Game.init();
        environment = new Environment("assets/maps/test.tmx");
        ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
        Game.getConfiguration().graphics().setFullscreen(false);
        GameController gameController = new GameController(environment,ingameScreen);

        Game.start();
    }
}
