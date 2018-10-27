package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import model.Screens.IngameScreen;
import model.Screens.MenuScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    public static void main(String[] args){
        new MainController();
    }

    private Environment environment;
    private Image cursor;

    public MainController(){
        Game.init();
        Game.getConfiguration().graphics().setFullscreen(false);
        try {
            cursor = ImageIO.read(new File("assets/img/cursor.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnte nicht geladen werden!");
        }
        Game.getScreenManager().getRenderComponent().setCursor(cursor);
        Game.load("assets/maps/game.litidata");
        IMap map1 = Game.getMap("map1");
        environment = new Environment(map1);
        Game.loadEnvironment(environment);
        Game.getScreenManager().addScreen(new MenuScreen());
        IngameScreen ingameScreen = new IngameScreen(environment);
        Game.getScreenManager().addScreen(ingameScreen);
        Game.getScreenManager().displayScreen(ingameScreen);
        GameController gameController = new GameController(environment,ingameScreen);
        Game.start();
    }
}
