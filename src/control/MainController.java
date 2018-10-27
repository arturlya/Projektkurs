package control;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.gui.screens.Resolution;
import model.IngameScreen;
import model.StaticData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    public static void main(String[] args){
        new MainController();
    }

    private IngameScreen ingameScreen;
    private Environment environment;
    private Image cursor;

    public MainController(){
        Game.init();
        Game.getConfiguration().graphics().setFullscreen(false);
        try {
            cursor = ImageIO.read(new File("assets/img/cursor.png"));
        } catch (IOException ex) {
            System.out.println("Bild konnrte nicht geladen werden!");
        }
        Game.getScreenManager().getRenderComponent().setCursor(cursor);
        Game.load("assets/maps/game.litidata");
        List<IMap> list = Game.getMaps();
        environment = new Environment(list.get(0));
        ingameScreen = new IngameScreen();
        Game.getScreenManager().addScreen(ingameScreen);
        GameController gameController = new GameController(environment,ingameScreen);
        Game.start();
    }
}
